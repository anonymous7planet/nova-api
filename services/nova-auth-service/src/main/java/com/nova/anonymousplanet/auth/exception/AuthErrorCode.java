package com.nova.anonymousplanet.auth.exception;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorGroupCode;
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
public enum AuthErrorCode implements ErrorCode {

    /**
     * ATH: 보안 및 토큰 상세 (Authentication)
     * Common의 UNAUTHORIZED보다 구체적인 사유를 명시
     */
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "001-01", "유효하지 않은 인증 토큰입니다.", DisplayTypeCode.ALERT),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "001-02", "인증 토큰이 존재하지 않습니다.", DisplayTypeCode.ALERT),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ErrorGroupCode.UNAUTHORIZED, "001-03", "인증 세션이 만료되었습니다. 다시 로그인해주세요.", DisplayTypeCode.ALERT),

    /**
     * USER: 회원 관리 비즈니스 (Business Domain)
     */
    REGISTER_FAILED(HttpStatus.BAD_REQUEST, ErrorGroupCode.USER, "002-01", "회원 가입 중 문제가 발생했습니다.", DisplayTypeCode.ALERT),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, ErrorGroupCode.USER, "002-02", "이미 사용 중인 이메일입니다.", DisplayTypeCode.ALERT),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, ErrorGroupCode.USER, "002-03", "비밀번호를 동일하게 입력해주세요.", DisplayTypeCode.ALERT),
    REJOIN_RESTRICTED(HttpStatus.FORBIDDEN, ErrorGroupCode.USER, "002-04", "탈퇴 후 30일 이내에는 재가입이 불가능합니다.", DisplayTypeCode.ALERT),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.USER, "002-05", "존재하지 않는 회원입니다.", DisplayTypeCode.ALERT),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, ErrorGroupCode.USER, "002-06", "아이디 또는 비밀번호가 올바르지 않습니다.", DisplayTypeCode.ALERT),
    USER_INACTIVE(HttpStatus.FORBIDDEN, ErrorGroupCode.USER, "002-07", "비활성화된 회원입니다.", DisplayTypeCode.ALERT),
    USER_WITHDRAWN(HttpStatus.GONE, ErrorGroupCode.USER, "002-08", "이미 탈퇴한 회원입니다.", DisplayTypeCode.ALERT),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorGroupCode.USER, "002-09", "사용자의 프로필 이미지를 찾을 수 없습니다.", DisplayTypeCode.ALERT),

    /**
     * SEC: 인프라/게이트웨이 보안
     */
    INVALID_GATEWAY_ACCESS(HttpStatus.FORBIDDEN, ErrorGroupCode.FORBIDDEN, "003-01", "비정상적인 접근이 감지되었습니다.", DisplayTypeCode.ALERT),

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
        return String.format("AUTH-%s-%s", this.groupCode, this.detailCode);
    }
}
