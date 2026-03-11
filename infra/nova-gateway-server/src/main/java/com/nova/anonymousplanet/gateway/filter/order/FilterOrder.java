package com.nova.anonymousplanet.gateway.filter.order;

import org.springframework.core.Ordered;

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
public interface FilterOrder {

    /**
     * -1: Exception Handler (GlobalErrorHandler)
     * → 스프링 WebFlux에서 WebExceptionHandler는 GlobalFilter보다 우선순위가 높음
     * → 모든 단계에서 발생하는 예외를 Catch하여 공통 포맷으로 응답.
     */
    int GLOBAL_ERROR_HANDLER = -1;

    /**
     * 0: TraceId 생성 필터
     * → 가장 먼저 실행되어 모든 로그와 요청 흐름을 추적 가능한 ID 저장
     * → 모든 요청에 대해 Trace-Id 생성 및 MDC 로그 설정.
     */
    int TRACE_ID = Ordered.HIGHEST_PRECEDENCE;


    /**
     * 5: Client IP / User-Agent 수집 필터
     * → 추적 ID가 있다면, 그 다음으로 사용자 정보 수집
     * → Client IP, User-Agent 등 접속 기기 정보 수집.
     */
    @Deprecated
    int CLIENT_INFO = 5;

    /**
     *
     * 20: Locale Resolution Filter
     * → 헤더 기반 Locale을 정해서 downstream 서비스로 전달
     * → 헤더의 Accept-Language 기반 다국어 설정 전달.
     */
    int LOCALE = 20;

    /**
     * 25: LocalMasterTokenFilter
     * → Local에서 테스트 편하게 하기 위해서
     * → (개발 환경 전용) 로컬 테스트용 마스터 권한 부여.
     */
    int LOCAL_MASTER = 25;


    /**
     * 40: UserHeaderFilter
     * → SecurityContext에서 UUID를 꺼내 Long ID 등으로 변환 후 하위 서비스용 헤더 주입.
     */
    int USER_HEADER = 40;

    // 30: JWT 인증 필터
    // → JWT 검증 및 사용자 정보(id, uuid, role, status, internal-auth) 헤더 주입, whiteList검사 포함
    @Deprecated
    int JWT_AUTH = 30;

    /**
     * 100: Access Log
     * → 정상/에러 포함 모든 요청 기록
     * → 최종 요청 처리 결과(Status Code) 및 수행 시간 기록.
     */
    int ACCESS_LOG = 100;


    /**
     * 110: Slow Log
     * → 처리 시간 임계치 초과 시만 기록
     * → 임계치 초과 요청에 대한 상세 경고 로그 기록.
     */
    int SLOW_LOG = 110;

}
