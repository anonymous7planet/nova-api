package com.nova.anonymousplanet.auth.exception.token;

import com.nova.anonymousplanet.auth.exception.AuthErrorCode;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.auth
 * fileName : ExpiredTokenException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */

public class ExpiredTokenException extends TokenException {

    public ExpiredTokenException(String logMessage) {
        super(AuthErrorCode.TOKEN_EXPIRED, logMessage);
    }

    public ExpiredTokenException() {
        this(AuthErrorCode.TOKEN_EXPIRED.getMessage());
    }

}
