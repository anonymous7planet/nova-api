package com.nova.anonymousplanet.core.constant.error;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * [Project Nova: Error Code System]
 * 1. 구성 원칙
 * - status: HTTP 상태 코드 (클라이언트 응답용)
 * - codeGroup: ErrorGroupCode 기반 분류 (Prefix)
 * - codeDetail: 상세 순번 (000 형식)
 * - titleMessage: 시스템/개발자용 에러 명칭 (로깅용)
 * - detailMessage: 사용자 노출용 메시지 (프론트엔드 노출용)
 *
 * * 2. 코드 체계 (Prefix)
 * - A (Auth): 인증 및 권한 관련 (401, 403)
 * - UR (User Register): 회원가입 및 계정 생성 관련 (400, 409)
 * - UI (User Info): 로그인, 조회 및 회원 상태 관련 (400, 404, 410)
 * - C (Common): 공통 유효성 검사 및 요청 관련 (400, 404)
 * - S (System): 서버 내부 장애, DB, 외부 연동 관련 (500)
 * * 3. 보안 가이드
 * - 500 계열(S)은 내부 스택트레이스나 DB 구조가 detailMessage에 노출되지 않도록 추상화함.
 *
 * 400 BAD_REQUEST: 잘못된 입력값
 * 403 FORBIDDEN: 권한 또는 상태 제한
 * 404 RESOURCE_NOT_FOUND: 존재하지 않는 리소스
 * 409 CONFLICT: 중복 리소스 (이메일, 닉네임 등)
 */

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    /**
     * BR / NF / CON / ATH / FOR: 기술 공통 (Technical)
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ErrorGroupCode.BAD_REQUEST, "400", "잘못 된 요청입니다.", DisplayTypeCode.ALERT),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "401", "로그인 후 다시 시도해주세요.", DisplayTypeCode.ALERT),
    FORBIDDEN(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "403", "접근 권한이 부족합니다.", DisplayTypeCode.ALERT),
    NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "404", "요청하신 정보를 찾을 수 없습니다.", DisplayTypeCode.ALERT),
    CONFLICT(HttpStatus.CONFLICT, ErrorGroupCode.CONFLICT, "409", "이미 존재하는 데이터이거나 상태가 맞지 않습니다.", DisplayTypeCode.ALERT),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "500", "서버 내부 장애가 발생했습니다.", DisplayTypeCode.ALERT),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, ErrorGroupCode.EXTERNAL, "502", "외부 서버의 응답이 올바르지 않습니다.", DisplayTypeCode.ALERT),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, ErrorGroupCode.EXTERNAL, "503", "서버 점검 중이거나 과부하 상태입니다.", DisplayTypeCode.ALERT),

    /**
     * VAL: 유효성 검증 (Validation)
     */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "001-01", "입력 값이 유효하지 않습니다. 정보를 확인해주세요.", DisplayTypeCode.ALERT),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "001-02", "정의되지 않은 코드값이 전달되었습니다.", DisplayTypeCode.ALERT),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "001-03", "요청 파라미터가 비즈니스 규칙에 어긋납니다.", DisplayTypeCode.ALERT),
    INVALID_ENUM(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "001-04", "정의되지 않은 코드값이 전달되었습니다.", DisplayTypeCode.ALERT),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "001-05","필드의 값이 유효하지 않습니다.([%s]에 코드 [%s]가 존재하지 않습니다.)", DisplayTypeCode.LOG),

    /**
     * SYS: 내부 장애 (Server Error)
     */
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "101-01", "데이터베이스 처리 중 오류가 발생했습니다.", DisplayTypeCode.ALERT),
    DATA_INTEGRITY_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "101-02", "데이터 정합성 문제가 발생했습니다.", DisplayTypeCode.ALERT),
    INFRASTRUCTURE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "101-03", "시스템 점검 중입니다. 서비스 이용에 불편을 드려 죄송합니다.", DisplayTypeCode.ALERT),
    MESSAGE_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "101-04", "이벤트 메시지 전송에 실패했습니다.", DisplayTypeCode.ALERT),

    /**
     * EXT: 외부 연동 (External)
     */
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.EXTERNAL, "201-01", "외부 서비스 연동에 실패했습니다.", DisplayTypeCode.ALERT),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, ErrorGroupCode.EXTERNAL, "504", "외부 서버 응답 대기 시간이 초과되었습니다.", DisplayTypeCode.ALERT),

    /**
     * ATH / FOR: 보안 상세 (Authentication & Access)
     */
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "002-01", "로그인이 필요한 서비스입니다.", DisplayTypeCode.ALERT),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "002-02", "해당 리소스에 접근할 권한이 없습니다.", DisplayTypeCode.ALERT),
    GATEWAY_SECRET_INVALID(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "003-01", "비정상적인 접근이 감지되었습니다.", DisplayTypeCode.ALERT)

    ;

    private final HttpStatus status;
    private final ErrorGroupCode groupCode;
    private final String detailCode;
    private final String message;
    private final DisplayTypeCode displayType;

    @Override
    public String getName() {
        return this.name();
    }


    @Override
    public String getCode() {
        return this.groupCode+this.detailCode;
    }

    @Override
    public String getFullCode() {
        // Prefix(모듈)-Group-Detail
        // COM은 Common의 약자로 전사 공통임을 의미
        return String.format("COM-%s-%s", this.groupCode, this.detailCode);
    }
}
