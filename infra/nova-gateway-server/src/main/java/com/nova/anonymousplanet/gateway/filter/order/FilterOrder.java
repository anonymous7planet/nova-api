package com.nova.anonymousplanet.gateway.filter.order;

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

    // 0: TraceId 생성 필터
    // → 가장 먼저 실행되어 모든 로그와 요청 흐름을 추적 가능한 ID 저장
    public static final int TRACE_ID = 0;

    // 5: Client IP / User-Agent 수집 필터
    // → 추적 ID가 있다면, 그 다음으로 사용자 정보 수집
    public static final int CLIENT_INFO = 5;

    // 20: Locale Resolution Filter
    // → 헤더 기반 Locale을 정해서 downstream 서비스로 전달
    public static final int LOCALE = 20;

    // 25: LocalMasterTokenFilter
    // Local에서 테스트 편하게 하기 위해서
    public static final int LOCAL_MASTER = 20;

    // 30: JWT 인증 필터
    // → JWT 검증 및 사용자 정보(id, uuid, role, status, internal-auth) 헤더 주입, whiteList검사 포함
    public static final int JWT_AUTH = 30;

    // 100: Access Log
    // → 정상/에러 포함 모든 요청 기록
    public static final int ACCESS_LOG = 100;

    // 110: Slow Log
    // → 처리 시간 임계치 초과 시만 기록
    public static final int SLOW_LOG = 110;

}
