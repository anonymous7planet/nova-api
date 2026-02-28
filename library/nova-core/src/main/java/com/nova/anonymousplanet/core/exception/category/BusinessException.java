package com.nova.anonymousplanet.core.exception.category;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.category
 * fileName : BusinessException
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * 비즈니스 로직 예외 (보통 4xx 계열)
 * - 원인이 되는 Throwable(cause)이 거의 없는 경우
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */
public class BusinessException extends NovaApplicationException {

    public BusinessException() {
        this(CommonErrorCode.BAD_REQUEST);
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public BusinessException(ErrorCode errorCode, String logMessage) {
        this(errorCode, null, logMessage);
    }

    public BusinessException(ErrorCode errorCode, String customMessage, String logMessage) {
        this(errorCode, errorCode.getMessage(), customMessage, logMessage);
    }

    public BusinessException(ErrorCode errorCode, String errorCustomMessage, String customMessage, String logMessage) {
        this(errorCode, errorCustomMessage, errorCode.getDisplayType(), customMessage, logMessage);
    }

    public BusinessException(ErrorCode errorCode, String errorCustomMessage, DisplayTypeCode displayType, String customMessage, String logMessage) {
        super(errorCode, errorCustomMessage, displayType, customMessage, logMessage);
    }

}
