package com.nova.anonymousplanet.auth.configuration.security;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.configuration.security
  fileName : SecurityConfiguration
  author : Jinhong Min
  date : 2025-11-04
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-04      Jinhong Min      최초 생성
  ==============================================
 */

import com.nova.anonymousplanet.auth.provider.GatewayIpProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final GatewayIpProvider gatewayIpProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // 1. 헬스 체크 경로는 무조건 허용 (모든 IP 허용)
                .requestMatchers("/actuator/**", "/health", "/info").permitAll()

                // 2. 나머지 모든 요청에 대해 게이트웨이 IP 검사 적용
                .anyRequest().access((authentication, context) -> {
                    String remoteAddr = context.getRequest().getRemoteAddr();
                    boolean allowed = gatewayIpProvider.isGatewayIp(remoteAddr);
                    return new AuthorizationDecision(allowed);
                })
            );

        return http.build();
    }
}
