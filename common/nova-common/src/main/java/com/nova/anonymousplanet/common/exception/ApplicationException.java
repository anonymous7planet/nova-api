package com.nova.anonymousplanet.common.exception;

import com.nova.anonymousplanet.common.constant.ErrorCode;
import lombok.Getter;


@Getter
public abstract class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String titleMessage;
    private final String detailMessage;

    protected ApplicationException(String message, ErrorCode errorCode, String titleMessage, String detailMessage) {
        super(message);
        this.errorCode = errorCode;
        this.titleMessage = titleMessage;
        this.detailMessage = detailMessage;
    }
}
