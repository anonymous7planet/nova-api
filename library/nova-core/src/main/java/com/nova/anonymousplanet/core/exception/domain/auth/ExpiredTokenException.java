package com.nova.anonymousplanet.core.exception.domain.auth;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;
import com.nova.anonymousplanet.core.exception.category.SecurityAuthException;
import org.springframework.expression.ExpressionInvocationTargetException;

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
public class ExpiredTokenException extends SecurityAuthException {

    public ExpiredTokenException() {
       super(ErrorCode.AUTH_TOKEN_EXPIRED);
    }

    public ExpiredTokenException(final String logMessage) {
        super(ErrorCode.AUTH_TOKEN_EXPIRED, logMessage);
    }
}
