package com.nova.anonymousplanet.core.exception.category;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.category
 * fileName : SecurityAuthException
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * 인증/인가 예외 (401, 403 계열)
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */
public class SecurityAuthException extends NovaApplicationException {

    public SecurityAuthException() {
        this(CommonErrorCode.AUTH_FORBIDDEN);
    }

    public SecurityAuthException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public SecurityAuthException(ErrorCode errorCode, String logMessage) {
        this(errorCode, null, logMessage);
    }

    public SecurityAuthException(ErrorCode errorCode, String customMessage, String logMessage) {
        this(errorCode, errorCode.getMessage(), customMessage, logMessage);
    }

    public SecurityAuthException(ErrorCode errorCode, String errorCustomMessage, String customMessage, String logMessage) {
        this(errorCode, errorCustomMessage, errorCode.getDisplayType(), customMessage, logMessage);
    }

    public SecurityAuthException(ErrorCode errorCode, String errorCustomMessage, DisplayTypeCode displayType, String customMessage, String logMessage) {
        super(errorCode, errorCustomMessage, displayType, customMessage, logMessage);
    }

}
