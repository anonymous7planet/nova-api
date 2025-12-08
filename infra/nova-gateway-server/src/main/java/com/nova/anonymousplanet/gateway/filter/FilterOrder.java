package com.nova.anonymousplanet.gateway.filter;

import java.util.Locale;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : FilterOrder
 * author : Jinhong Min
 * date : 2025-11-15
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-15      Jinhong Min      최초 생성
 * ==============================================
 */
public class FilterOrder {
    private FilterOrder() {}


    // -1: Exception Handler (GlobalErrorHandler)
    // → 스프링 WebFlux에서 WebExceptionHandler는 GlobalFilter보다 우선순위가 높음
    public static final int EXCEPTION_HANDLER = -1;

    // 0: TraceId / RequestId 생성 필터
    // → 가장 먼저 실행되어 모든 로그와 요청 흐름을 추적 가능한 ID 저장
    public static final int TRACE_ID = 0;

    // 5: Client IP / User-Agent 수집 필터
    // → 추적 ID가 있다면, 그 다음으로 사용자 정보 수집
    public static final int CLIENT_INFO = 5;

    // 10: 공통 Request Logging Filter
    // → 요청 시작/종료 로그 기록
    public static final int LOGGING = 10;

    // 20: Locale Resolution Filter
    // → 헤더 기반 Locale을 정해서 downstream 서비스로 전달
    public static final int LOCALE = 20;

    // 30: JWT 인증 필터
    // → JWT 검증 및 사용자 정보(id, uuid, role, status, internal-auth) 헤더 주입, whiteList검사 포함
    public static final int JWT_AUTH = 30;

}
