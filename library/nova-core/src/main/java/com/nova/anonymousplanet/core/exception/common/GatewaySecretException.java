package com.nova.anonymousplanet.core.exception.common;

import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;

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
public class GatewaySecretException extends NovaApplicationException {
    public GatewaySecretException() {
        super(CommonErrorCode.GATEWAY_SECRET_INVALID);
    }

    public GatewaySecretException(final String logMessage){
        super(CommonErrorCode.GATEWAY_SECRET_INVALID, logMessage);
    }
}
