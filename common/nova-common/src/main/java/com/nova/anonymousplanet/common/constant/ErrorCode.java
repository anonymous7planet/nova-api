package com.nova.anonymousplanet.common.constant;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "A000", "잘못된 요청입니다.", "요청 파라미터를 확인해주세요."),

    /**
     * Token Error
     */
    TOKEN_NOT_EXISTS(HttpStatus.BAD_REQUEST, "A001-1", "토큰이 존재 하지 않습니다.", "다시 로그인해주세요."),
    TOKEN_ILLEGAL(HttpStatus.BAD_REQUEST, "A001-2", "해당 토큰은 잘못 된 토큰입니다.", "다시 로그인해주세요."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "A001-3", "해당 토큰은 만료된 토큰입니다.", "다시 로그인해주세요."),
    TOKEN_MALFORMED(HttpStatus.BAD_REQUEST, "A001-4", "해당 토큰은 잘못된 JWT 서명입니다.", "다시 로그인해주세요."),
    TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST, "A001-5", "해당 토큰은 지원 되지 않는 JWT 토큰입니다.", "다시 로그인해주세요."),

    /**
     * validation Error
     */
    VALIDATION(HttpStatus.BAD_REQUEST, "A002-0", "잘못된 입력 값이 존재 합니다.", "입력 값을 확인 해주세요, 잘못된 입력 값이 존재 합니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "B000", "로그인이 필요한 요청입니다.", "로그인 해주세요."),

    FORBIDDEN(HttpStatus.FORBIDDEN, "C000", "접근 권한이 없습니다.", "접근할수 없습니다 권한을 확인해주세요."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "D000", "자원을 찾을 수 없습니다.", "자원을 찾을 수 없습니다."),

    /**
     * 공통 NOT FOUND
     */
    INVALID_ENUM_CODE(HttpStatus.NOT_FOUND, "D001-0", "잘못된 코드입니다.", "Enum Type [%s]에 코드 [%s]가 존재하지 않습니다."),


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S000", "서버에서 에러가 발생했습니다.", "서버 내부에서 오류가 발생했습니다.")
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

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getCode() {
        return this.code;
    }

    public String getTitleMessage() {
        return this.titleMessage;
    }

    public String getDetailMessage() {
        return this.detailMessage;
    }
}
