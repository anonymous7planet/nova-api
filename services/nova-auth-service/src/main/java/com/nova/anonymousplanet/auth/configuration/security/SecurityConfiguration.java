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

import com.nova.anonymousplanet.security.configuration.NovaSecurityConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final NovaSecurityConfigurer novaConfigurer;

    // 각 서비스 고정 FREE_PATHS(Gateway서버에도 무조건 등록 필요)
    private static final String[] SERVICE_FREE_PATHS = {
            "/v1/signup",
            "/v1/login",
            "/v1/token/refresh"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. 라이브러리가 제공하는 공통 설정(CSRF, Session, Filter) 적용
        return novaConfigurer.applyCommonConfig(http)
                .authorizeHttpRequests(auth -> {
                    if (SERVICE_FREE_PATHS.length > 0) {
                        auth.requestMatchers(SERVICE_FREE_PATHS).permitAll();
                    }
                })
                .build();
    }
}
