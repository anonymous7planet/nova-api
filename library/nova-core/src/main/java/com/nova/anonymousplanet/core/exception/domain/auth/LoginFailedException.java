package com.nova.anonymousplanet.core.exception.domain.auth;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.BusinessException;
import lombok.Getter;

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

@Getter
public class LoginFailedException extends BusinessException {
    public LoginFailedException() {
        super(ErrorCode.USER_LOGIN_FAILED);
    }

    public LoginFailedException(final String logMessage) {
        super(ErrorCode.USER_LOGIN_FAILED, logMessage);
    }
}
