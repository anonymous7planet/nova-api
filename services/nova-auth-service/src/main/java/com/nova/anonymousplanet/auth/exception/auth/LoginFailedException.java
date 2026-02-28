package com.nova.anonymousplanet.auth.exception.auth;

import com.nova.anonymousplanet.auth.exception.AuthErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.BusinessException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.auth
 * fileName : LoginFailedException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */

public class LoginFailedException extends BusinessException {

    public LoginFailedException(ErrorCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    public LoginFailedException(String logMessage) {
        this(AuthErrorCode.LOGIN_FAILED, logMessage);
    }

    public LoginFailedException() {
        this(null);
    }

}
