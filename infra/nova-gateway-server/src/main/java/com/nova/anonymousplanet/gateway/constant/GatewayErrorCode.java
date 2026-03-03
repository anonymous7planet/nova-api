package com.nova.anonymousplanet.gateway.constant;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorGroupCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

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
public enum GatewayErrorCode implements ErrorCode {

    // --- [400] Client Side Errors ---
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, ErrorGroupCode.BAD_REQUEST, "4001", "JSON 요청 바디를 읽을 수 없습니다.", DisplayTypeCode.CONFIRM),

    // --- [401] Authentication (보안 전략 반영) ---
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "4010", "로그인이 필요한 서비스입니다.", DisplayTypeCode.NONE),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "001-01", "유효하지 않은 인증 토큰입니다.", DisplayTypeCode.ALERT),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "001-02", "인증 토큰이 존재하지 않습니다.", DisplayTypeCode.ALERT),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "001-03", "인증 세션이 만료되었습니다. 다시 로그인해주세요.", DisplayTypeCode.ALERT),

    // --- [403] Forbidden / Security ---
    FORBIDDEN(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "4030", "해당 리소스에 접근할 권한이 없습니다.", DisplayTypeCode.ALERT),
    INVALID_GATEWAY_SECRET(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "4031", "보안 검증(Secret) 실패했습니다.", DisplayTypeCode.ALERT),
    IP_RESTRICTED(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "4032", "허용되지 않은 IP에서의 접근입니다.", DisplayTypeCode.ALERT),

    // --- [404] Not Found ---
    SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "4040", "요청한 서비스가 존재하지 않습니다.", DisplayTypeCode.ALERT),
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "4041", "요청한 API 경로를 찾을 수 없습니다.", DisplayTypeCode.ALERT),
    METHOD_NOT_ALLOWED(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "G4050", "지원하지 않는 HTTP 메소드입니다.", DisplayTypeCode.LOG),

    // --- [429] Rate Limit ---
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, ErrorGroupCode.BAD_REQUEST, "4290", "요청 횟수를 초과했습니다. 잠시 후 시도해주세요.", DisplayTypeCode.TOAST),

    // --- [500] Server Side Errors ---
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "5000", "시스템 오류가 발생했습니다.", DisplayTypeCode.ALERT),

    // --- [503] Availability / Infrastructure ---
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, ErrorGroupCode.SERVER_ERROR, "5030", "연결된 서비스가 응답할 수 없는 상태입니다.", DisplayTypeCode.ALERT),
    CIRCUIT_BREAKER_OPEN(HttpStatus.SERVICE_UNAVAILABLE, ErrorGroupCode.SERVER_ERROR, "5031", "서킷 브레이커에 의해 연결이 차단되었습니다.", DisplayTypeCode.ALERT),
    LOAD_BALANCER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, ErrorGroupCode.SERVER_ERROR, "G5032", "가용한 서비스 인스턴스를 찾을 수 없습니다.", DisplayTypeCode.LOG),

    // --- [504] Timeout ---
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, ErrorGroupCode.SERVER_ERROR, "5040", "대상 서비스 응답 지연으로 요청이 취소되었습니다.", DisplayTypeCode.ALERT),

    ;

    private final HttpStatus status;           // HTTP 상태 코드
    private final ErrorGroupCode groupCode;    // Prefix용 그룹 (ATH, FOR 등)
    private final String detailCode;           // 상세 코드 (4010, 4031 등)
    private final String message;              // 사용자 노출 메시지
    private final DisplayTypeCode displayType; // 에러 노출 방식 (ALERT, TOAST, MODAL 등)

    public String getName() {
        return this.name();
    }


    public String getCode() {
        return this.groupCode+this.detailCode;
    }

    @Override
    public String getFullCode() {
        // Prefix(모듈)-Group-Detail
        // COM은 Common의 약자로 전사 공통임을 의미
        return String.format("GATE-%s-%s", this.groupCode, this.detailCode);
    }
}