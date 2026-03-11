package com.nova.anonymousplanet.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.model.response.NovaErrorResponse;
import com.nova.anonymousplanet.core.model.response.NovaResponse;
import com.nova.anonymousplanet.core.util.PathUtils;
import com.nova.anonymousplanet.gateway.configuration.properties.NovaGatewaySecurityProperties;
import com.nova.anonymousplanet.gateway.constant.GatewayErrorCode;
import com.nova.anonymousplanet.gateway.dto.RefreshTokenStoreDto;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import com.nova.anonymousplanet.gateway.service.jwt.JwtRefreshTokenStore;
import com.nova.anonymousplanet.gateway.service.jwt.JwtTokenProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Deprecated
@Slf4j
//@Component
public class JwtAuthenticationGatewayFilter extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilter.Config> implements Ordered {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRefreshTokenStore jwtRefreshTokenStore;
    private final ObjectMapper objectMapper; // 💡 ObjectMapper를 필드로 정의하여 재사용

    private static final String BEARER_PREFIX = "Bearer ";

    public JwtAuthenticationGatewayFilter(JwtTokenProvider jwtTokenProvider, JwtRefreshTokenStore jwtRefreshTokenStore, ObjectMapper objectMapper) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtRefreshTokenStore = jwtRefreshTokenStore;
        this.objectMapper = objectMapper; // ObjectMapper 주입
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            // 요청 URL
            String requestPath = exchange.getRequest().getURI().getPath();

            // 1. JWT검증 필요 없을 경우 (Excluded Path)
            if (config.getNovaGatewaySecurityProperties().isFreePath(requestPath)) {
                log.debug("[JwtAuthenticationGatewayFilter] Excluded path: {}", requestPath);
                return chain.filter(exchange);
            }


            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String traceId = request.getHeaders().getFirst(LogContextCode.TRACE_ID.getHeaderKey());


            // 2. Header에 Authorization 필드 유무 확인
            if (!containsAuthorization(request)) {
                return onError(response, traceId, requestPath, GatewayErrorCode.UNAUTHORIZED);
            }

            // 3. Authorization에서 accessToken값 추출&존재유무 확인
            String accessToken = extractAccessToken(request);
            if (!StringUtils.hasText(accessToken)) {
                return onError(response, traceId, requestPath, GatewayErrorCode.TOKEN_NOT_FOUND);
            }

            // 4. accessToken값 validation (JWT 유효성 검사)
            Map<String, String> errorMap = jwtTokenProvider.validateAccessToken(accessToken);
            if (errorMap != null) {
                return onError(response, traceId, requestPath, GatewayErrorCode.TOKEN_INVALID);
            }

            // 5. Redis 유효성 검증 (RefreshToken 유효성 검사)
            String userUuid = jwtTokenProvider.getUserUuid(accessToken);
            String userRole = jwtTokenProvider.getRole(accessToken);
            String deviceId = jwtTokenProvider.getDeviceId(accessToken);

            boolean valid = jwtRefreshTokenStore.validate(new RefreshTokenStoreDto.ValidateRequest(userUuid, deviceId));
            if (!valid) {
                return onError(response, traceId, requestPath, GatewayErrorCode.TOKEN_INVALID);
            }

            // 6. Redis에서 추가 정보 조회 및 Header 추가
            RefreshTokenStoreDto.GetResponse redisStore = jwtRefreshTokenStore.get(
                    new RefreshTokenStoreDto.GetRequest(userUuid, deviceId))
                .orElseThrow(() -> new RuntimeException("Redis store data not found after validation."));

            addAuthorizationHeaders(request, redisStore.userId(), userUuid, userRole, redisStore.userStatus());

            return chain.filter(exchange);
        });
    }

    /**
     * Authorization필드 있는지 확인
     */
    private boolean containsAuthorization(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }


    /**
     * Header에 Authorization필드에서 AccessToken값 추출
     */
    private String extractAccessToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return "";
    }


    /**
     * 타서비스로 보낼 Request Header에 필드 추가
     * @param request
     * @param userId
     * @param userUuid
     * @param userRoleCode   : code값
     * @param userStatusCode : code값
     */
    private void addAuthorizationHeaders(ServerHttpRequest request, Long userId, String userUuid, String userRoleCode, String userStatusCode) {
        request.mutate()
            .header("X-User-Id", String.valueOf(userId))
            .header("X-User-Uuid", userUuid)
            .header("X-User-Role", userRoleCode)
            .header("X-User-Status", userStatusCode)
            .header("X-Internal-Auth", "NOVA-GATEWAY")
            .build()
        ;
    }


    /**
     * 오류 발생 시 RestGatewayResponse DTO 형식으로 JSON 응답을 반환합니다.
     */
    private Mono<Void> onError(ServerHttpResponse response, String traceId, String path, ErrorCode errorCode) {
        response.setStatusCode(errorCode.getStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        NovaResponse<Void> errorBody = NovaResponse.fail("실패", traceId, traceId, path, NovaErrorResponse.of(errorCode));

        DataBuffer buffer = null;
        try {
            // ObjectMapper를 사용하여 DTO를 JSON 바이트로 변환
            byte[] bytes = objectMapper.writeValueAsBytes(errorBody);
            buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("[JwtFilter] Error during JSON serialization: {}", e.getMessage());
            // JSON 변환 실패 시 비상 응답
            buffer = response.bufferFactory().wrap("{\"message\":\"JSON serialization failed\"}".getBytes());
            return response.writeWith(Mono.just(buffer));
        }
    }

    @Getter
    @Setter
    public static class Config {
        private NovaGatewaySecurityProperties novaGatewaySecurityProperties;
    }

    @Override
    public int getOrder() {
        // 필터 순서 정의
        return FilterOrder.JWT_AUTH;
    }
}
