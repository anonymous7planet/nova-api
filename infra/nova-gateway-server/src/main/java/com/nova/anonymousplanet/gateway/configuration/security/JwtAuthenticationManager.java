package com.nova.anonymousplanet.gateway.configuration.security;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.gateway.configuration.security.model.UserPrincipal;
import com.nova.anonymousplanet.gateway.constant.GatewayErrorCode;
import com.nova.anonymousplanet.gateway.dto.RefreshTokenStoreDto;
import com.nova.anonymousplanet.gateway.exception.NovaGatewayAuthException;
import com.nova.anonymousplanet.gateway.service.jwt.JwtRefreshTokenStore;
import com.nova.anonymousplanet.gateway.service.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration.security
 * fileName : JwtAuthenticationManager
 * author : Jinhong Min
 * date : 2026-03-10
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-10      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRefreshTokenStore jwtRefreshTokenStore;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        String authToken = authentication.getCredentials().toString();

        return Mono.just(authToken)
                .flatMap(token -> {
                    // 1. JwtTokenProvider를 통한 상세 검증
                    Map<String, String> errorMap = jwtTokenProvider.validateAccessToken(token);

                    // 2. 에러가 존재한다면 상세 메시지와 함께 예외 발생
                    if (errorMap != null && !errorMap.isEmpty()) {
                        log.error(">>>> [JwtAuthenticationManager] Validation Failed: {}", errorMap.get("message"));
                        ErrorCode errorCode = GatewayErrorCode.fromCode(errorMap.get("code"));
                        return Mono.error(new NovaGatewayAuthException(errorCode, errorMap.get("message")));
                    }
                    String uuid = jwtTokenProvider.getUserUuid(token);
                    String userRole = jwtTokenProvider.getRole(token);
                    String deviceId = jwtTokenProvider.getDeviceId(token);

                    // 2. Redis 유효성 검증 및 추가 정보 조회 (Reactive Flow)
                    // Redis 조회를 Mono로 래핑하여 비동기 처리
                    return Mono.fromCallable(() -> jwtRefreshTokenStore.validate(new RefreshTokenStoreDto.ValidateRequest(uuid, deviceId)))
                            .flatMap(isValid -> {
                                if (!isValid) {
                                    return Mono.error(new NovaGatewayAuthException(GatewayErrorCode.TOKEN_INVALID));
                                }

                                // 3. Redis에서 상세 정보(userId, status) 가져오기
                                return Mono.fromCallable(() -> jwtRefreshTokenStore.get(new RefreshTokenStoreDto.GetRequest(uuid, deviceId))
                                        .orElseThrow(() -> new NovaGatewayAuthException(GatewayErrorCode.USER_NOT_FOUND)));
                            })
                            .map(redisStore -> {
                                // 4. 모든 정보가 취합된 최종 UserPrincipal 생성
                                UserPrincipal principal = new UserPrincipal(
                                        redisStore.userId(),
                                        uuid,
                                        userRole,
                                        redisStore.userStatus(),
                                        deviceId
                                );

                                log.debug(">>>> [AuthManager] Redis 검증 완료 - User: {}, Status: {}", uuid, redisStore.userStatus());

                                return new UsernamePasswordAuthenticationToken(
                                        principal,
                                        null,
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + principal.role()))
                                );
                            })
                            .onErrorResume(e -> {
                                if (e instanceof NovaGatewayAuthException) return Mono.error(e);
                                log.error(">>>> [AuthManager] Unexpected Error: {}", e.getMessage());
                                return Mono.error(new NovaGatewayAuthException(GatewayErrorCode.UNAUTHORIZED));
                            });
                });

    }

}
