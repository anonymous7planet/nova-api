package com.nova.anonymousplanet.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.gateway.dto.response.RestGatewayResponse;
import com.nova.anonymousplanet.gateway.dto.RefreshTokenStoreDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.integration.annotation.Gateway;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationGatewayFilter extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilter.Config> implements Ordered {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRefreshTokenStore jwtRefreshTokenStore;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    private static final String BEARER_PREFIX = "Bearer ";

    public JwtAuthenticationGatewayFilter(JwtTokenProvider jwtTokenProvider, JwtRefreshTokenStore jwtRefreshTokenStore) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtRefreshTokenStore = jwtRefreshTokenStore;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            // 요청 URL
            String requestPath = exchange.getRequest().getURI().getPath();

            // JWT검증 필요 없을 경우
            if (isExcluded(requestPath, config.getExcludedPaths())) {
                log.debug("[JwtFilter] Excluded path: {}", requestPath);
                return chain.filter(exchange);
            }


            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();


            // Header에 Authorization 필드 유무 확인
            if (!containsAuthorization(request)) {
                return onError(
                    response
                    , "잘못된 요청입니다."
                    , new RestGatewayResponse.GatewayErrorSet(requestPath, "G001", "[NOVA][Gateway] Header에 Authorization필드가 존재하지 않습니다.")
                    , HttpStatus.UNAUTHORIZED
                );
            }

            // Authorization에서 accessToken값 추출&존재유무 확인
            String accessToken = extractAccessToken(request);
            if (!StringUtils.hasText(accessToken)) {
                return onError(
                    response
                    , "잘못된 요청입니다."
                    , new RestGatewayResponse.GatewayErrorSet(requestPath, "G001", "[NOVA][Gateway] Header에 accessToken이 존재하지 않습니다.")
                    , HttpStatus.UNAUTHORIZED
                );
            }
            // accessToken값 validation
            Map<String, String> errorMap = jwtTokenProvider.validateAccessToken(accessToken);
            if (errorMap != null) {
                return onError(
                    response
                    , errorMap.get("message")
                    , new RestGatewayResponse.GatewayErrorSet(requestPath, "G001", "[NOVA][GateWay] " + errorMap.get("detailMessage"))
                    , HttpStatus.BAD_REQUEST
                );
            }

            // header에 userId, userUuid, userRole, userStatus추가
            // accessToken에 userUuid와 userRole, deviceId가 있고, userId와 userStatus는 redis에있다
            String userUuid = jwtTokenProvider.getUserUuid(accessToken);
            String userRole = jwtTokenProvider.getRole(accessToken);
            String deviceId = jwtTokenProvider.getDeviceId(accessToken);

            boolean valid = jwtRefreshTokenStore.validate(new RefreshTokenStoreDto.ValidateRequest(userUuid, deviceId));
            if (!valid) {
                return onError(
                    response
                    , "토큰에 문제가 말생했습니다."
                    , new RestGatewayResponse.GatewayErrorSet(requestPath, "G001", "[NOVA][GateWay] 토큰 정보에 문제가 발생했습니다.")
                    , HttpStatus.BAD_REQUEST
                );
            }

            RefreshTokenStoreDto.GetResponse redisStore = jwtRefreshTokenStore.get(
                new RefreshTokenStoreDto.GetRequest(userUuid, deviceId)).get();

            addAuthorizationHeaders(request, redisStore.userId(), userUuid, userRole, redisStore.userStatus());

            return chain.filter(exchange);
        });
    }


    /**
     * URL검증
     *
     * @param path
     * @param excludedPaths
     * @return
     */
    private boolean isExcluded(String path, List<String> excludedPaths) {
        return excludedPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }


    /**
     * Authorization필드 있는지 확인
     *
     * @param request
     * @return
     */
    private boolean containsAuthorization(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }


    /**
     * Header에 Authorization필드에서 AccessToken값 추출
     *
     * @param request
     * @return
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
     *
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


    private Mono<Void> onError(ServerHttpResponse response, String message, RestGatewayResponse.GatewayErrorSet error, HttpStatus status) {
        response.setStatusCode(status);

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer = null;
        try {
            buffer = response.bufferFactory().wrap(new ObjectMapper().writeValueAsBytes(
                RestGatewayResponse.error(message, error)));
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return response.writeWith(Mono.just(buffer));
        }

    }

    @Getter
    @Setter
    public static class Config {
        private List<String> excludedPaths;

        public Config() {
            this.excludedPaths = List.of(
                "/v1/signup",
                "/v1/login",
                "/v1/token/refresh",
                "/v1/health"
            );
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
