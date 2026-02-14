package com.nova.anonymousplanet.core.exception.category;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.category
 * fileName : InvalidRequestException
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
public class InvalidRequestException extends NovaApplicationException {
    public InvalidRequestException() {
        super(ErrorCode.VALIDATION_ERROR, ErrorCode.VALIDATION_ERROR.getDetailMessage());
    }

    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDetailMessage());
    }

    public InvalidRequestException(ErrorCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    public InvalidRequestException(ErrorCode errorCode, String logMessage, String title, String detail) {
        super(errorCode, logMessage, title, detail);
    }

    public InvalidRequestException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause.getMessage(), cause);
    }
}
