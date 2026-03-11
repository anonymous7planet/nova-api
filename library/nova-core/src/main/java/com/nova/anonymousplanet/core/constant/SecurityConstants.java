package com.nova.anonymousplanet.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

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

    // [1] 시스템 및 모니터링 관련 경로
    public static final List<String> MONITORING_PATHS = List.of(
            "/health",
            "/info",
            "/nova/management/**"
    );

    // [2] Swagger 및 API 문서 관련 경로(FIXME: 중복 제거 및 수정 필요)
    public static final List<String> SWAGGER_PATHS = List.of(
            // 1. 서비스 공통 (Root 및 서비스별 경로 대응)
//            "/v3/api-docs/**",
//            "/*/v3/api-docs/**",
//
//            // 2. UI 및 정적 리소스
//            "/swagger-ui/**",
//            "/*/swagger-ui/**",
//            "/swagger-ui.html",
//            "/*/swagger-ui.html",
//            "/webjars/**",
//            "/*/webjars/**",
//
//            // 3. 동적 구성 및 설정
//            "/swagger-resources/**",
//            "/*/swagger-resources/**",
//            "/swagger-dynamic/**",
//            "/*/swagger-dynamic/**",
//            "/configuration/**",
//            "/*/configuration/**"
            "/v3/api-docs/**",
            "/*/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/*/v3/api-docs/**",      // 각 서비스별 api-docs (예: /auth/v3/api-docs)
            "/swagger-ui/**",
            "/*/swagger-ui/**",       // 서비스별 swagger-ui 경로 대응
            "/swagger-resources/**",
            "/*/swagger-resources/**",
            "/swagger-ui.html",
            "/*/swagger-ui.html",
            "/webjars/**",
            "/*/webjars/**",
            "/swagger-dynamic/**",    // 질문하신 에러의 직접적인 원인
            "/*/swagger-dynamic/**",
            "/configuration/**",      // 스웨거 설정을 위한 경로
            "/*/configuration/**"
    );

    // [3] 정적 리소스 및 파비콘
    public static final List<String> STATIC_RESOURCE_PATHS = List.of(
            "/.well-known",
            "/favicon.ico",
            "/favicon-32x32.png",
            "/favicon-16x16.png"
    );

    // [4] 규약 기반 공개 API 패턴 (Project Nova 표준)
    public static final List<String> PUBLIC_API_PATTERNS = List.of(
            "/api/*/v1/*/public/**", // gateway
            "/v1/*/public/**"        // 각 서비스
    );

    // [최종] 전사 공통 허용 경로 통합 (Filter에서 참조)
    public static final List<String> SYSTEM_FREE_PATH_PATTERNS = Stream.of(
                    MONITORING_PATHS,
                    SWAGGER_PATHS,
                    STATIC_RESOURCE_PATHS,
                    PUBLIC_API_PATTERNS
            )
            .flatMap(List::stream)
            .toList();


    // FilterRegistrationBean 등 서블릿 필터용 (별표 하나 패턴으로 변환용)
    public static final List<String> SWAGGER_SERVLET_FREE_PATH_PATTERNS = List.of(
            "/v3/api-docs/*",
            "/swagger-ui/*",
            "/swagger-ui.html"
    );

    public static final List<String> SERVLET_FREE_PATH_PATTERNS = Stream.of(
                SWAGGER_SERVLET_FREE_PATH_PATTERNS
            )
            .flatMap(List::stream)
            .toList();
}
