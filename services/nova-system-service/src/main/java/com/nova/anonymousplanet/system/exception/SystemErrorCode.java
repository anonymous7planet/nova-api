package com.nova.anonymousplanet.system.exception;

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
public enum SystemErrorCode implements ErrorCode {

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
        return String.format("SYS-%s-%s", this.groupCode, this.detailCode);
    }
}
