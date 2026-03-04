package com.nova.anonymousplanet.core.filter;

import org.springframework.core.Ordered;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.filter
 * fileName : FilterOrder
 * author : Jinhong Min
 * date : 2026-03-04
 * description :
 * 모든 filter 순서 관리
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-04      Jinhong Min      최초 생성
 * ==============================================
 */
public interface FilterOrder {
    // 1단계: 인프라/로깅 계층 (MDC 정보 주입)
    int TRACE_FILTER = Ordered.HIGHEST_PRECEDENCE;          // 최우선
    int MDC_SETUP_FILTER = TRACE_FILTER + 1;                // 로그 설정

    // 2단계: 보안 검증 계층 (인증 필터 전처리)
    int SWAGGER_KEY_FILTER = MDC_SETUP_FILTER + 10;         // 로그 설정 직후 검증
    int GATEWAY_SECRET_FILTER = SWAGGER_KEY_FILTER + 10;

    // 3단계: 비즈니스 공통 계층
    int COMMON_CONTEXT_FILTER = 0;

    // 4단계: 마무리 계층 (MDC 정리)
    int MDC_CLEAR_FILTER = Ordered.LOWEST_PRECEDENCE;
}
