package com.nova.anonymousplanet.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration
 * fileName : CorsConfiguration
 * author : Jinhong Min
 * date : 2025-11-09
 * description :
 * 주요 옵션 정리:
 * - allowedOrigins: 허용 Origin 리스트
 * - allowedHeaders: 허용 헤더 리스트
 * - allowedMethods: 허용 HTTP 메서드
 * - allowCredentials: 쿠키 인증 허용 여부
 * - maxAge: preflight 결과 캐시 시간(초)
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-09      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
public class CorsCustomConfiguration {

    @Bean
    public CorsConfigurationSource corsCustomConfig() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ 허용할 Origin
//        config.setAllowedOrigins(Arrays.asList(
//            "http://localhost:3000",
//            "https://nova.app"
//        ));

        // ✅ 허용 메서드 (필요 시 OPTIONS 추가)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ 허용 헤더
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));

        // ✅ 쿠키 허용
        config.setAllowCredentials(true);

        // ✅ Preflight 캐시 유지 시간 (초)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
