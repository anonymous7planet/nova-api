package com.nova.anonymousplanet.core.exception.category;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.category
 * fileName : BusinessException
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
public class BusinessException extends NovaApplicationException {

    // 1. 기본 생성자
    public BusinessException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getDetailMessage());
    }

    // 2. ErrorCode 중심
    public BusinessException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDetailMessage());
    }

    // 3. 로그 메시지 포함 (실무 권장)
    public BusinessException(ErrorCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    // 4. 클라이언트 응답 메시지 완전 제어
    public BusinessException(ErrorCode errorCode, String logMessage, String customTitle, String customDetail) {
        super(errorCode, logMessage, customTitle, customDetail);
    }

    // 5. 예외 체이닝 (Cause 포함) - 원인 분석용
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause.getMessage(), cause);
    }
}