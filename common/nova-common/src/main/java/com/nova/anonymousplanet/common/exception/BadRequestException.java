package com.nova.anonymousplanet.common.exception;


import com.nova.anonymousplanet.common.constant.error.ErrorCode;

/**
 * 4XX에러(클라이언트에러)
 */
public class BadRequestException extends ApplicationException {

    public BadRequestException(String message, ErrorCode errorCode, String titleMessage, String detailMessage) {
        super(message, errorCode, titleMessage, detailMessage);
    }

    public BadRequestException(String message, ErrorCode errorCode) {
        this(message, errorCode, errorCode.getTitleMessage(), errorCode.getDetailMessage());
    }

    public BadRequestException(String message) {
        this(message, ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getTitleMessage(), ErrorCode.BAD_REQUEST.getDetailMessage());
    }

    public BadRequestException() {
        this(ErrorCode.BAD_REQUEST.getTitleMessage(), ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getTitleMessage(), ErrorCode.BAD_REQUEST.getDetailMessage());
    }
}
