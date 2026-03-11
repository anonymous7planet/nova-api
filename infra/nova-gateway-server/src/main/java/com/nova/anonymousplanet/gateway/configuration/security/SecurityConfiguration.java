package com.nova.anonymousplanet.gateway.configuration.security;

import com.nova.anonymousplanet.gateway.configuration.properties.NovaGatewaySecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration.security
 * fileName : SecurityConfiguration
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
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final GatewayAuthenticationEntryPoint gatewayAuthenticationEntryPoint;

    private final NovaGatewaySecurityProperties novaGatewaySecurityProperties;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // 1. CSRF, Form Login, HTTP Basic 등 불필요한 설정 비활성화 (Stateless)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // 2. 예외 처리 종착역 설정
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(gatewayAuthenticationEntryPoint)
                )

                // 3. 경로별 권한 설정
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(novaGatewaySecurityProperties.getFinalFreePaths().toArray(String[]::new)).permitAll() // 화이트리스트 허용
                        .anyExchange().authenticated()        // 그 외 모든 요청은 인증 필요
                )

                // 4. JWT 필터 추가 (가장 핵심)
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    /**
     * JWT 전용 인증 필터 생성
     * Converter를 통해 요청에서 토큰을 추출하고, Manager를 통해 검증을 수행합니다.
     */
    private AuthenticationWebFilter jwtAuthenticationFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtAuthenticationManager);

        // 요청 헤더에서 토큰을 꺼내 UsernamePasswordAuthenticationToken으로 변환하는 전략 주입
        filter.setServerAuthenticationConverter(jwtAuthenticationConverter);

        // (선택 사항) 특정 경로에만 이 필터를 적용하고 싶을 때 사용
        // [이게 필요한 이유] 인증 필터 자체가 작동할 '범위'를 지정하는 것입니다.
        // 화이트리스트 경로에서는 아예 이 필터(JWT 검증)가 실행되지 않도록 차단하는 역할입니다.

        // 1. 화이트리스트 경로 매처 생성
        ServerWebExchangeMatcher freePathsMatcher = ServerWebExchangeMatchers.pathMatchers(
                novaGatewaySecurityProperties.getFinalFreePaths().toArray(String[]::new)
        );

        // 2. [강력 추천] 화이트리스트가 '아닌(Negated)' 경우만 인증 필터 작동
        // 이 방식은 내부적인 matches() 호출과 결과 반전을 Spring이 알아서 처리합니다.
        filter.setRequiresAuthenticationMatcher(new NegatedServerWebExchangeMatcher(freePathsMatcher));

        return filter;
    }
}
