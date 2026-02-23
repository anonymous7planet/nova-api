package com.nova.anonymousplanet.core.exception;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception
 * fileName : NovaException
 * author : Jinhong Min
 * date : 2026-02-13
 * description :
 * [Project Nova - 1st Depth]
 * 최상위 추상 예외 클래스입니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-13      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
public abstract class NovaApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String customTitle;
    private final String customDetail;
    private final String logMessage;


    /**
     *
     * 기본 생성자: ErrorCode의 기본 메시지 사용
     * @param errorCode
     * @param message
     * @param logMessage
     */
    protected NovaApplicationException(ErrorCode errorCode, String message, String logMessage) {
        super(logMessage);
        this.errorCode = errorCode;
        this.customTitle = errorCode.getTitleMessage();
        this.customDetail = errorCode.getDetailMessage();
        this.logMessage = logMessage;
    }

    protected NovaApplicationException(ErrorCode errorCode, String logMessage) {
        super(logMessage);
        this.errorCode = errorCode;
        this.customTitle = errorCode.getTitleMessage();
        this.customDetail = errorCode.getDetailMessage();
        this.logMessage = logMessage;
    }

    /**
     * 커스텀 메시지 생성자: 상황에 따라 제목과 상세 내용을 재정의
     * @param errorCode
     * @param message
     * @param customTitle
     * @param customDetail
     * @param logMessage
     */
    protected NovaApplicationException(ErrorCode errorCode, String message, String customTitle, String customDetail, String logMessage) {
        super(logMessage);
        this.errorCode = errorCode;
        this.customTitle = customTitle;
        this.customDetail = customDetail;
        this.logMessage = logMessage;
    }

    protected NovaApplicationException(ErrorCode errorCode, String customTitle, String customDetail, String logMessage) {
        super(logMessage);
        this.errorCode = errorCode;
        this.customTitle = customTitle;
        this.customDetail = customDetail;
        this.logMessage = logMessage;
    }

    protected NovaApplicationException(ErrorCode errorCode, String message, String logMessage, Throwable cause) {
        super(logMessage, cause); // RuntimeException의 cause로 전달
        this.errorCode = errorCode;
        this.customTitle = errorCode.getTitleMessage();
        this.customDetail = errorCode.getDetailMessage();
        this.logMessage = logMessage;
    }

    protected NovaApplicationException(ErrorCode errorCode, String logMessage, Throwable cause) {
        super(logMessage, cause); // RuntimeException의 cause로 전달
        this.errorCode = errorCode;
        this.customTitle = errorCode.getTitleMessage();
        this.customDetail = errorCode.getDetailMessage();
        this.logMessage = logMessage;
    }

    public String getResponseTitle() {
        return (customTitle != null) ? customTitle : errorCode.getTitleMessage();
    }

    public String getResponseDetail() {
        return (customDetail != null) ? customDetail : errorCode.getDetailMessage();
    }
}
