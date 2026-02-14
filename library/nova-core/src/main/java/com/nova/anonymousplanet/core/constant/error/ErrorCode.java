package com.nova.anonymousplanet.core.constant.error;

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
 * 404 NOT_FOUND: 존재하지 않는 리소스
 * 409 CONFLICT: 중복 리소스 (이메일, 닉네임 등)
 */

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * BR / NF / CON / ATH / FOR: 기술 공통 (Technical)
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ErrorGroupCode.BAD_REQUEST, "400", "잘못된 요청입니다.", "요청 파라미터를 확인해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.NOT_FOUND, "404", "리소스를 찾을 수 없습니다.", "요청하신 정보를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, ErrorGroupCode.CONFLICT, "409", "데이터 충돌이 발생했습니다.", "이미 존재하는 데이터이거나 상태가 맞지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "401", "인증이 필요합니다.", "로그인 후 다시 시도해주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "403", "권한이 없습니다.", "접근 권한이 부족합니다."),

    /**
     * VAL: 유효성 검증 (Validation)
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "001-01", "입력값 검증 실패", "입력 값이 유효하지 않습니다. 정보를 확인해주세요."),
    INVALID_ENUM(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "001-02", "잘못된 코드값", "정의되지 않은 코드값이 전달되었습니다."),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "001-03", "잘못된 코드값", "정의되지 않은 코드값이 전달되었습니다.(Enum Type [%s]에 코드 [%s]가 존재하지 않습니다.)"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, ErrorGroupCode.VALIDATION, "C001-04", "부적절한 파라미터", "요청 파라미터가 비즈니스 규칙에 어긋납니다."),

    /**
     * ATH / FOR: 보안 상세 (Authentication & Access)
     */
    AUTH_TOKEN_NOT_EXISTS(HttpStatus.BAD_REQUEST, ErrorGroupCode.UNAUTHORIZED, "001-01", "토큰 부재", "인증 토큰이 존재하지 않습니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "001-02", "토큰 만료", "인증 세션이 만료되었습니다."),
    AUTH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "001-03", "유효하지 않은 토큰", "변조되거나 잘못된 인증 토큰입니다."),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "002-01", "인증 실패", "로그인이 필요한 서비스입니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "002-02", "권한 부족", "해당 리소스에 접근할 권한이 없습니다."),

    GATEWAY_SECRET_INVALID(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "003-01", "보안 정책 위반", "비정상적인 접근이 감지되었습니다."),

    /**
     * USER: 회원 관리 비즈니스 (Business Domain)
     */
    USER_REGISTER_FAILED(HttpStatus.BAD_REQUEST, ErrorGroupCode.USER, "001-01", "회원 가입 실패", "회원 가입 중 문제가 발생했습니다."),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, ErrorGroupCode.USER, "001-02", "이메일 중복", "이미 사용 중인 이메일입니다."),

    USER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, ErrorGroupCode.USER, "002-01", "비밀번호 불일치", "비밀번호를 동일하게 입력해주세요."),

    USER_REJOIN_RESTRICTED(HttpStatus.FORBIDDEN, ErrorGroupCode.USER, "003-01", "재가입 제한", "탈퇴 후 30일 이내에는 재가입이 불가능합니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.USER, "004-01", "회원 정보 없음", "존재하지 않는 회원입니다."),

    USER_LOGIN_FAILED(HttpStatus.BAD_REQUEST, ErrorGroupCode.USER, "005-01", "로그인 실패", "아이디 또는 비밀번호가 올바르지 않습니다."),

    USER_INACTIVE(HttpStatus.FORBIDDEN, ErrorGroupCode.USER, "006-01", "계정 비활성화", "비활성화된 회원입니다."),

    USER_ALREADY_WITHDRAWN(HttpStatus.GONE, ErrorGroupCode.USER, "007-01", "탈퇴 계정", "이미 탈퇴한 회원입니다."),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.USER, "007-02", "프로필 이미지 없음", "사용자의 프로필 이미지를 찾을 수 없습니다."),

    /**
     * SYS: 내부 장애 (Server Error)
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "500", "서버 오류", "서버 내부 장애가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "001-01", "DB 오류", "데이터베이스 처리 중 오류가 발생했습니다."),
    DATA_INTEGRITY_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "001-02", "무결성 위반", "데이터 정합성 문제가 발생했습니다."),

    MESSAGE_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "002-02", "메시지 발행 실패", "이벤트 메시지 전송에 실패했습니다."),

    INFRASTRUCTURE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.SERVER_ERROR, "003-01", "시스템 연동 오류", "서비스 이용에 불편을 드려 죄송합니다. 시스템 점검 중입니다."),

    /**
     * EXT: 외부 연동 (External)
     */
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, ErrorGroupCode.EXTERNAL, "502", "게이트웨이 오류", "외부 서버의 응답이 올바르지 않습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, ErrorGroupCode.EXTERNAL, "503", "서비스 이용 불가", "서버 점검 중이거나 과부하 상태입니다."),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, ErrorGroupCode.EXTERNAL, "504", "시간 초과", "외부 서버 응답 대기 시간이 초과되었습니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGroupCode.EXTERNAL, "001-01", "연동 실패", "외부 서비스 연동에 실패했습니다.")

    ;

    private final HttpStatus status;
    private final ErrorGroupCode codeGroup;
    private final String codeDetail;
    private final String titleMessage;
    private final String detailMessage;

    public String getName() {
        return this.name();
    }

    public String getCode() {
        return this.codeGroup.getCode() + "-" + this.codeDetail;
    }
}
