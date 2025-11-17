package com.nova.anonymousplanet.gateway.filter;

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

    // 0: 요청 추적 및 로그 상관관계 ID 주입 (가장 먼저 실행)
    public static final int REQUEST_ID = 0;
    // 10: 요청 시작/종료 로그 기록
    public static final int LOGGING = 10;
    // 30: JWT 검증 및 사용자 정보 전달 (Whitelist 검사 포함)
    public static final int JWT_AUTH = 30;
    // -1: WebExceptionHandler는 보통 GlobalFilter보다 높은 우선순위를 갖습니다.
    public static final int EXCEPTION_HANDLER = -1;


}
