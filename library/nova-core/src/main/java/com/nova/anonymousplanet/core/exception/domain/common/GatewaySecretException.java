package com.nova.anonymousplanet.core.exception.domain.common;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.SecurityAuthException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.common
 * fileName : GatewaySecretException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */
public class GatewaySecretException extends SecurityAuthException {
    public GatewaySecretException() {
        super(ErrorCode.GATEWAY_SECRET_INVALID);
    }

    public GatewaySecretException(final String logMessage){
        super(ErrorCode.GATEWAY_SECRET_INVALID, logMessage);
    }
}
