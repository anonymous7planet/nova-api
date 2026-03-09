package com.nova.anonymousplanet.security.configuration;

import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.core.constant.SecurityConstants;
import com.nova.anonymousplanet.security.filter.NovaSecurityFilter;
import com.nova.anonymousplanet.security.handler.NovaAccessDeniedHandler;
import com.nova.anonymousplanet.security.handler.NovaAuthenticationEntryPoint;
import com.nova.anonymousplanet.security.provider.DiscoveryIpProvider;
import com.nova.anonymousplanet.security.provider.GatewayIpProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
    private final List<String> FREE_PATHS;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(FREE_PATHS.toArray(String[]::new)); // Swagger 등 공통 화이트리스트 제외
    }

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
                    // 인증 필요 없는 경로
                    auth.requestMatchers(FREE_PATHS.toArray(String[]::new)).permitAll();
                    // IP 검증 로직 공통화
                    auth.anyRequest().access((authentication, context) -> {
                        String remoteAddr = context.getRequest().getRemoteAddr();
                        String serviceId = context.getRequest().getHeader(LogContextCode.SERVICE_NAME.getHeaderKey());

                        // Gateway IP이거나, 혹은 Eureka에 등록된 신뢰할 수 있는 서비스의 IP인가?
                        boolean isAllowed = gatewayIpProvider.isGatewayIp(remoteAddr) ||
                                discoveryIpProvider.isTrustedService(remoteAddr, serviceId);
                        return new AuthorizationDecision(isAllowed);
                    });
                });
    }
}
