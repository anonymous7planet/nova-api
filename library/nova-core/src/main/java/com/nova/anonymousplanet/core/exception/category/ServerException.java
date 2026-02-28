package com.nova.anonymousplanet.core.exception.category;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.category
 * fileName : ServerException
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * 시스템 및 인프라 예외 (보통 5xx 계열)
 * - DB, 네트워크, 외부 API 등 '당한' 에러라 cause 포함이 권장됨
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */

public class ServerException extends NovaApplicationException {

    public ServerException(Throwable cause) {
       this(CommonErrorCode.INTERNAL_SERVER_ERROR, cause);
    }

    // 시스템 에러는 원인(cause)을 추적하는 것이 핵심이므로 cause 포함 생성자 위주
    public ServerException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, null, cause);
    }

    public ServerException(ErrorCode errorCode, String logMessage, Throwable cause) {
        this(errorCode, null, logMessage, cause);
    }

    public ServerException(ErrorCode errorCode, String customMessage, String logMessage, Throwable cause) {
        this(errorCode, errorCode.getMessage(), customMessage, logMessage, cause);
    }

    public ServerException(ErrorCode errorCode, String errorCustomMessage, String customMessage, String logMessage, Throwable cause) {
        this(errorCode, errorCustomMessage, errorCode.getDisplayType(), customMessage, logMessage, cause);
    }

    public ServerException(ErrorCode errorCode, String errorCustomMessage, DisplayTypeCode displayType, String customMessage, String logMessage, Throwable cause) {
        super(errorCode, errorCustomMessage, displayType, customMessage, logMessage, cause);
    }

}
