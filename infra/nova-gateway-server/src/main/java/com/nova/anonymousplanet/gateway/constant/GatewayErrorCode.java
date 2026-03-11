package com.nova.anonymousplanet.gateway.constant;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorGroupCode;
import com.nova.anonymousplanet.core.util.EnumUtils;
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
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, ErrorGroupCode.BAD_REQUEST, "001-01", "JSON 요청 바디를 읽을 수 없습니다.", DisplayTypeCode.CONFIRM),

    // --- [401] Authentication (보안 전략 반영) ---
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "002-01", "로그인이 필요한 서비스입니다.", DisplayTypeCode.NONE),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "002-02", "유효하지 않은 인증 토큰입니다.", DisplayTypeCode.ALERT),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "002-03", "인증 토큰이 존재하지 않습니다.", DisplayTypeCode.ALERT),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "002-04", "인증 세션이 만료되었습니다. 다시 로그인해주세요.", DisplayTypeCode.ALERT),

    // --- [403] Forbidden / Security ---
    INVALID_GATEWAY_SECRET(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "003-01", "보안 검증(Secret) 실패했습니다.", DisplayTypeCode.ALERT),
    IP_RESTRICTED(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "003-02", "허용되지 않은 IP에서의 접근입니다.", DisplayTypeCode.ALERT),

    // --- [404] Not Found ---
    SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "004-01", "요청한 서비스가 존재하지 않습니다.", DisplayTypeCode.ALERT),
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "004-02", "요청한 API 경로를 찾을 수 없습니다.", DisplayTypeCode.ALERT),
    METHOD_NOT_ALLOWED(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "004-03", "지원하지 않는 HTTP 메소드입니다.", DisplayTypeCode.LOG),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "004-04", "회원정보를 찾을 수 없습니다.", DisplayTypeCode.LOG),

    // --- [503] Availability / Infrastructure ---
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, ErrorGroupCode.SERVER_ERROR, "005-01", "연결된 서비스가 응답할 수 없는 상태입니다.", DisplayTypeCode.ALERT),
    CIRCUIT_BREAKER_OPEN(HttpStatus.SERVICE_UNAVAILABLE, ErrorGroupCode.SERVER_ERROR, "005-02", "서킷 브레이커에 의해 연결이 차단되었습니다.", DisplayTypeCode.ALERT),
    LOAD_BALANCER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, ErrorGroupCode.SERVER_ERROR, "005-03", "가용한 서비스 인스턴스를 찾을 수 없습니다.", DisplayTypeCode.LOG),


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
        return this.groupCode + "-" + this.detailCode;
    }

    @Override
    public String getFullCode() {
        // Prefix(모듈)-Group-Detail
        // COM은 Common의 약자로 전사 공통임을 의미
        return String.format("GATE-%s-%s", this.groupCode, this.detailCode);
    }

    /**
     * rawCode값은 groupCode+"-"+detailCode
     * @param rawCode
     * @return
     */
    public static GatewayErrorCode fromCode(String rawCode) {
        return EnumUtils.fromCode(GatewayErrorCode.class, rawCode);
    }
}