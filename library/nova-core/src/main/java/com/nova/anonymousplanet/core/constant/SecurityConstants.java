package com.nova.anonymousplanet.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : SecurityConstants
 * author : Jinhong Min
 * date : 2026-03-05
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-05      Jinhong Min      최초 생성
 * ==============================================
 */


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {
    // 전사 공통 허용 경로 (Spring Ant-style 패턴)
    public static final String[] COMMON_WHITE_LIST = {
            "/health",
            "/info",
            "/nova/management/**",

            "/v3/api-docs/**",    // Swagger/OpenAPI v3 스펙(API 명세 JSON 데이터)
            "/swagger-ui/**",      // Swagger UI 리소스(index.css, swagger-ui-bundle.js 등 모든 정적 리소스 포함)
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/.well-known",
            "/webjars/**",         // Swagger UI 내부 정적 자원
            "/*/v3/api-docs/**", // 각 마이크로서비스의 docs 경로
            "/favicon.ico",
            "/favicon-32x32.png",
            "/favicon-16x16.png",
    };

    // FilterRegistrationBean 등 서블릿 필터용 (별표 하나 패턴으로 변환용)
    public static final String[] SERVLET_WHITE_LIST = {
            "/v3/api-docs/*",    // Swagger/OpenAPI v3 스펙(API 명세 JSON 데이터)
            "/swagger-ui/*",
            "/swagger-ui.html"
    };
}
