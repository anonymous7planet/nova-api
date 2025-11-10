package com.nova.anonymousplanet.gateway.configuration;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.gateway.configuration
  fileName : SecurityConfiguration
  author : Jinhong Min
  date : 2025-11-09
  description :
 * 주요 옵션 정리:
 * - csrf(): CSRF 비활성화 (API 서버는 Stateless)
 * - cors(): CORS 허용
 * - authorizeExchange(): 요청별 접근 제어
 * - httpBasic(): HTTP Basic 인증
 * - formLogin(): 폼 로그인 사용
 * - oauth2Login(): OAuth2 로그인
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-09      Jinhong Min      최초 생성
  ==============================================
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
}
