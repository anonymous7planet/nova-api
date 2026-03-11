package com.nova.anonymousplanet.gateway.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration.security
 * fileName : JwtAuthenticationConverter
 * author : Jinhong Min
 * date : 2026-03-10
 * description :
 * Header에서 토큰 꺼내서 전달하는 역할
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-10      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        // 1. 헤더에서 Authorization 추출
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                // 2. "Bearer " 접두사가 있는지 필터링
                .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                // 3. 접두사를 제거하고 순수 JWT만 추출
                .map(authHeader -> authHeader.substring(BEARER_PREFIX.length()))
                // 4. 검증 전용 Authentication 객체 생성 (Principal과 Credentials에 토큰을 담음)
                // 아직 검증 전이므로 credentials(토큰)만 담아서 AuthenticationManager로 전달
                .map(token -> new UsernamePasswordAuthenticationToken(token, token));
    }
}
