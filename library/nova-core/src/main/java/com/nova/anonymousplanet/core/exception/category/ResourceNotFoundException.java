package com.nova.anonymousplanet.core.exception.category;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.category
 * fileName : ResourceNotFoundException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * [Phase 2] 자원 부재 카테고리 예외
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
public class ResourceNotFoundException extends NovaApplicationException {

    // 1. 기본 생성자
    public ResourceNotFoundException() {
        super(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getDetailMessage());
    }

    // 2. ErrorCode 중심 생성자
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDetailMessage());
    }

    // 3. 로그 메시지 포함 생성자
    public ResourceNotFoundException(ErrorCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    // 4. 클라이언트 응답 메시지 완전 제어 생성자
    public ResourceNotFoundException(ErrorCode errorCode, String logMessage, String title, String detail) {
        super(errorCode, logMessage, title, detail);
    }

    // 5. 예외 체이닝 생성자
    public ResourceNotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause.getMessage(), cause);
    }
}
