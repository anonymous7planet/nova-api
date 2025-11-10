package com.nova.anonymousplanet.core.exception;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import lombok.Getter;


@Getter
public abstract class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String titleMessage;
    private final String detailMessage;

    /**
     *
     * @param message
     * @param errorCode 에러 코드
     * @param titleMessage 타이틀 메시지(내용 없을 경우 ErrorCode에 있는 titleMessage사용)
     * @param detailMessage 상세 메시지(내용 없을 경우 ErrorCode에 있는 detailMessage사용)
     */
    protected ApplicationException(String message, ErrorCode errorCode, String titleMessage, String detailMessage) {
        super(message);
        this.errorCode = errorCode;
        this.titleMessage = titleMessage;
        this.detailMessage = detailMessage;
    }
}
