package com.nova.anonymousplanet.core.exception.category;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.category
 * fileName : SecurityAuthException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * [Phase 2] 보안 및 인증 관련 예외 카테고리
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
public class SecurityAuthException extends NovaApplicationException {

    public SecurityAuthException() {
        super(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getDetailMessage());
    }

    public SecurityAuthException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDetailMessage());
    }

    public SecurityAuthException(ErrorCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    public SecurityAuthException(ErrorCode errorCode, String logMessage, String title, String detail) {
        super(errorCode, logMessage, title, detail);
    }

    public SecurityAuthException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause.getMessage(), cause);
    }

}
