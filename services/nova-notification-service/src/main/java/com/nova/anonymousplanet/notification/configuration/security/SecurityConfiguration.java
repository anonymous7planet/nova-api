package com.nova.anonymousplanet.notification.configuration.security;

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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final NovaSecurityConfigurer novaConfigurer;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. 라이브러리가 제공하는 공통 설정(CSRF, Session, Filter) 적용
        return novaConfigurer.applyCommonConfig(http).build();
    }
}
