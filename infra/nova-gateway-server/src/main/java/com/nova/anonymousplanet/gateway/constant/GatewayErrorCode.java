package com.nova.anonymousplanet.gateway.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.constant
 * fileName : ErrorCode
 * author : Jinhong Min
 * date : 2026-01-30
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-30      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
@RequiredArgsConstructor
public enum GatewayErrorCode {
    // --- [400] Client Side Errors ---
    INVALID_REQUEST("G4000", "잘못된 요청", "요청 파라미터가 누락되었거나 형식이 올바르지 않습니다."),
    INVALID_JSON_FORMAT("G4001", "메시지 파싱 실패", "JSON 요청 바디를 읽을 수 없습니다."),

    // --- [401] Authentication (보안 전략 반영) ---
    UNAUTHORIZED("G4010", "인증 필요", "로그인이 필요한 서비스입니다."),
    INVALID_TOKEN("G4011", "유효하지 않은 토큰", "Access Token이 만료되었거나 변조되었습니다."),
    TOKEN_SIGNATURE_INVALID("G4012", "토큰 서명 오류", "인증 서버의 서명과 일치하지 않는 토큰입니다."),

    // --- [403] Forbidden / Security ---
    FORBIDDEN("G4030", "접근 거부", "해당 리소스에 접근할 권한이 없습니다."),
    INVALID_GATEWAY_SECRET("G4031", "보안 검증 실패", "X-Gateway-Secret이 올바르지 않습니다."), // Nova 보안 전략
    IP_RESTRICTED("G4032", "IP 제한", "허용되지 않은 IP 주소에서의 접근입니다."),

    // --- [404] Not Found ---
    SERVICE_NOT_FOUND("G4040", "서비스 찾을 수 없음", "요청한 서비스가 Eureka에 등록되어 있지 않습니다."),
    ROUTE_NOT_FOUND("G4041", "경로 찾을 수 없음", "요청한 API 엔드포인트를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("G4050", "메소드 불허용", "지원하지 않는 HTTP 메소드입니다."),

    // --- [429] Rate Limit ---
    TOO_MANY_REQUESTS("G4290", "요청 횟수 초과", "허용된 호출 횟수를 초과했습니다. 잠시 후 다시 시도해주세요."),

    // --- [500] Server Side Errors ---
    INTERNAL_SERVER_ERROR("G5000", "시스템 오류", "서버 내부 로직 처리 중 예기치 못한 오류가 발생했습니다."),
    DATABASE_ERROR("G5001", "데이터베이스 오류", "시스템 정보 조회 중 오류가 발생했습니다."),

    // --- [503] Availability / Infrastructure ---
    SERVICE_UNAVAILABLE("G5030", "서비스 일시 불가", "연결된 마이크로서비스가 현재 응답할 수 없는 상태입니다."),
    CIRCUIT_BREAKER_OPEN("G5031", "서킷 브레이커 작동", "대상 서비스 장애로 인해 연결이 차단되었습니다."),
    LOAD_BALANCER_ERROR("G5032", "로드밸런싱 실패", "가용한 서비스 인스턴스를 찾을 수 없습니다."),

    // --- [504] Timeout ---
    GATEWAY_TIMEOUT("G5040", "응답 시간 초과", "대상 서비스로부터 응답이 지연되어 요청이 취소되었습니다.");


    private final String code;
    private final String titleMessage;
    private final String detailMessage;
}