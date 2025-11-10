package com.nova.anonymousplanet.core.exception;


import com.nova.anonymousplanet.core.constant.error.ErrorCode;

/**
 * 500에러(서버에러)
 */
public class InternalServerErrorException extends ApplicationException {

    public InternalServerErrorException(String message, ErrorCode errorCode, String titleMessage, String detailMessage) {
        super(message, errorCode, titleMessage, detailMessage);
    }
    public InternalServerErrorException(String message, ErrorCode errorCode) {
        this(message, errorCode, errorCode.getTitleMessage(), errorCode.getDetailMessage());
    }

    public InternalServerErrorException(String message) {
        this(message, ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getTitleMessage(), ErrorCode.INTERNAL_SERVER_ERROR.getDetailMessage());
    }

    public InternalServerErrorException() {
        this(ErrorCode.INTERNAL_SERVER_ERROR.getTitleMessage(), ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getTitleMessage(), ErrorCode.INTERNAL_SERVER_ERROR.getDetailMessage());
    }
}
