package com.nova.anonymousplanet.auth.exception.token;

import com.nova.anonymousplanet.auth.exception.AuthErrorCode;

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

public class NotFoundTokenException extends TokenException {

    public NotFoundTokenException(String logMessage) {
        super(AuthErrorCode.TOKEN_NOT_FOUND, logMessage);
    }

    public NotFoundTokenException() {
        this(null);
    }

}
