package com.nova.anonymousplanet.security.configuration;

import com.nova.anonymousplanet.security.constant.HeaderContextCode;
import com.nova.anonymousplanet.security.filter.NovaSecurityFilter;
import com.nova.anonymousplanet.security.handler.NovaAccessDeniedHandler;
import com.nova.anonymousplanet.security.handler.NovaAuthenticationEntryPoint;
import com.nova.anonymousplanet.security.provider.DiscoveryIpProvider;
import com.nova.anonymousplanet.security.provider.GatewayIpProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.configuration
 * fileName : NovaSecurityConfigurer
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */

@RequiredArgsConstructor
public class NovaSecurityConfigurer {
    private final NovaSecurityFilter novaSecurityFilter;
    private final GatewayIpProvider gatewayIpProvider;
    private final DiscoveryIpProvider discoveryIpProvider;
    private final NovaAuthenticationEntryPoint novaAuthenticationEntryPoint;
    private final NovaAccessDeniedHandler novaAccessDeniedHandler;

    // application.yml에서 서비스 전용 화이트리스트를 읽어옴. 값이 없으면 빈 리스트로 초기화.
    private final String[] serviceWhiteList;

    // 전사 공통 허용 경로 (IP 검증 및 인증 제외)
    private static final String[] COMMON_WHITE_LIST = {
            "/health",
            "/info",
            "/actuator/**",
            "/v3/api-docs/**",    // Swagger/OpenAPI v3 스펙
            "/swagger-ui/**",      // Swagger UI 리소스
            "/swagger-resources/**",
            "/webjars/**"          // Swagger UI 내부 정적 자원
    };


    public HttpSecurity applyCommonConfig(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(novaAuthenticationEntryPoint) // 깔끔한 연결
                        .accessDeniedHandler(novaAccessDeniedHandler)           // 깔끔한 연결
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(novaSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    // 공통 화이트리스트 및 서비스별 화이트리스트 허용 (인증 & IP 체크 제외)
                    auth.requestMatchers(COMMON_WHITE_LIST).permitAll();
                    // 2. 서비스별 화이트리스트 적용 (null 체크 및 비어있는지 확인)
                    if (serviceWhiteList != null && !(serviceWhiteList.length == 0)) {
                        // 리스트를 배열로 변환하여 적용
                        auth.requestMatchers(serviceWhiteList).permitAll();
                    }
                    // IP 검증 로직 공통화
                    auth.anyRequest().access((authentication, context) -> {
                        String remoteAddr = context.getRequest().getRemoteAddr();
                        String serviceId = context.getRequest().getHeader(HeaderContextCode.SERVICE_NAME.getHeaderKey());

                        // Gateway IP이거나, 혹은 Eureka에 등록된 신뢰할 수 있는 서비스의 IP인가?
                        boolean isAllowed = gatewayIpProvider.isGatewayIp(remoteAddr) ||
                                discoveryIpProvider.isTrustedService(remoteAddr, serviceId);
                        return new AuthorizationDecision(isAllowed);
                    });
                });
    }
}
