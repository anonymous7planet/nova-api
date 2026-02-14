package com.nova.anonymousplanet.core.exception.domain.auth;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.SecurityAuthException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.auth
 * fileName : InvalidTokenException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
public class InvalidTokenException extends SecurityAuthException {

    public InvalidTokenException() {
        super(ErrorCode.AUTH_TOKEN_INVALID);
    }

    public InvalidTokenException(final String  logMessage) {
        super(ErrorCode.AUTH_TOKEN_INVALID, logMessage);
    }
}
