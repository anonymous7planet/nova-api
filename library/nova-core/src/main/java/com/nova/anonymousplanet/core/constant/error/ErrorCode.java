package com.nova.anonymousplanet.core.constant.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 400 BAD_REQUEST: 잘못된 입력값
 * 403 FORBIDDEN: 권한 또는 상태 제한
 * 404 NOT_FOUND: 존재하지 않는 리소스
 * 409 CONFLICT: 중복 리소스 (이메일, 닉네임 등)
 */

@Getter
public enum ErrorCode {

    /**
     * 토큰 Error
     */
    TOKEN_NOT_EXISTS(HttpStatus.BAD_REQUEST, "T001-01", "토큰이 존재 하지 않습니다.", "다시 로그인해주세요."),
    TOKEN_ILLEGAL(HttpStatus.BAD_REQUEST, "T001-02", "해당 토큰은 잘못 된 토큰입니다.", "다시 로그인해주세요."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "T001-03", "해당 토큰은 만료된 토큰입니다.", "다시 로그인해주세요."),
    TOKEN_MALFORMED(HttpStatus.BAD_REQUEST, "T001-04", "해당 토큰은 잘못된 JWT 서명입니다.", "다시 로그인해주세요."),
    TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST, "T001-05", "해당 토큰은 지원 되지 않는 JWT 토큰입니다.", "다시 로그인해주세요."),

    /**
     * 회원가입 및 회원 관련 Error
     */
    USER_REGISTER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "U001-01", "회원 가입 중 문제가 발생했습니다.", "회원 가입 중 문제가 발생 했습니다."),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U001-02", "이미 사용 중인 이메일입니다.", "다른 이메일을 입력해주세요."),
    USER_NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "U001-03", "이미 사용 중인 닉네임입니다.", "다른 닉네임을 입력해주세요."),
    USER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "U001-04", "비밀번호가 일치하지 않습니다.", "비밀번호와 비밀번호 확인란을 동일하게 입력해주세요."),


    /**
     * 회원 관련 Error
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002-01", "회원정보를 찾을 수 없습니다.", "회원 정보를 확인해주세요."),
    USER_INACTIVE(HttpStatus.FORBIDDEN, "U002-02", "비활성화된 회원입니다.", "관리자 승인 후 이용 가능합니다."),
    USER_ALREADY_WITHDRAWN(HttpStatus.GONE, "U002-03", "탈퇴한 회원입니다.", "재가입이 필요합니다."),
    USER_STATUS_NOT_ALLOWED(HttpStatus.FORBIDDEN, "U002-04", "현재 상태에서는 요청을 처리할 수 없습니다.", "회원 상태를 확인해주세요."),

    /**
     * Validation Error
     */
    VALIDATION(HttpStatus.BAD_REQUEST, "V001-01", "잘못된 입력 값이 존재 합니다.", "입력 값을 확인 해주세요, 잘못된 입력 값이 존재 합니다."),

    /**
     * 공통 Error
     */
    INVALID_ENUM_CODE(HttpStatus.NOT_FOUND, "D001-00", "잘못된 코드입니다.", "Enum Type [%s]에 코드 [%s]가 존재하지 않습니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "B000", "잘못된 요청입니다.", "요청 파라미터를 확인해주세요."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A000", "로그인이 필요한 요청입니다.", "로그인 해주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "C000", "접근 권한이 없습니다.", "접근할수 없습니다 권한을 확인해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "D000", "자원을 찾을 수 없습니다.", "자원을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S000", "서버에서 에러가 발생했습니다.", "서버 내부에서 오류가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String titleMessage;
    private final String detailMessage;

    ErrorCode(HttpStatus status, String code, String titleMessage, String detailMessage) {
        this.status = status;
        this.code = code;
        this.titleMessage = titleMessage;
        this.detailMessage = detailMessage;
    }

    public String getName() {
        return this.name();
    }
}
