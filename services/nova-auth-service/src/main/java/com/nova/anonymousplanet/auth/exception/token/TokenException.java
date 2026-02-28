package com.nova.anonymousplanet.auth.exception.token;

import com.nova.anonymousplanet.auth.exception.AuthErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.BusinessException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.exception.token
 * fileName : TokenException
 * author : Jinhong Min
 * date : 2026-02-28
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-28      Jinhong Min      최초 생성
 * ==============================================
 */

public class TokenException extends BusinessException {

    public TokenException(ErrorCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    public TokenException(String logMessage) {
        this(AuthErrorCode.TOKEN_INVALID, logMessage);
    }

    public TokenException() {
        this(AuthErrorCode.TOKEN_INVALID.getMessage());
    }

}
