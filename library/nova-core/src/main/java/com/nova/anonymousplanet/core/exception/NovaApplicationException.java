package com.nova.anonymousplanet.core.exception;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

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
    private final String errorCustomMessage;    // 사용자용: 클라이언트에 전달할 메시지((ErrorCode에 있는 message) - UI전략에 따라 노출
    private final DisplayTypeCode displayType;  // UI 전략: ALERT, TOAST, PAGE 등
    private final String customMessage;         // 개발자용: 개발자에게 전달할 메시지(NovaResponse에 있는 message)
    private final String logMessage;            // 개발자용: 서버 로그에 남길 상세 내용
    private final Map<String, Object> context;  // 운영용: 필요 시 에러 시점 데이터 보관


    // --- 1. 가장 기본: ErrorCode 정보만 사용 ---
    protected NovaApplicationException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    // --- 2. 로그 메시지만 커스텀 (디버깅용 상세 정보 추가) ---
    protected NovaApplicationException(ErrorCode errorCode, String logMessage) {
        this(errorCode, null, logMessage);
    }

    // --- 2. 로그와 사용자 메시지만 커스텀 (디버깅용 상세 정보 추가) ---
    protected NovaApplicationException(ErrorCode errorCode, String customMessage, String logMessage) {
        this(errorCode, errorCode.getMessage(), customMessage, logMessage);
    }

    // --- 3. 로그와 사용자 메시지와 error메시지까지 모두 커스텀 ---
    protected NovaApplicationException(ErrorCode errorCode, String errorCustomMessage, String customMessage, String logMessage) {
        this(errorCode, errorCustomMessage, errorCode.getDisplayType(), customMessage, logMessage);
    }

    // --- 4. UI 전략(DisplayType)까지 커스텀 ---
    protected NovaApplicationException(ErrorCode errorCode, String errorCustomMessage, DisplayTypeCode displayType, String customMessage, String logMessage) {
        this(errorCode, errorCustomMessage, displayType, customMessage, logMessage, null);
    }

    // --- 5. 예외 연쇄(Cause) 포함 ---
    protected NovaApplicationException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, errorCode.getMessage(), errorCode.getDisplayType(), null, null, cause);
    }

    // --- 6. 최상위 생성자 (모든 필드 초기화) ---
    protected NovaApplicationException(ErrorCode errorCode, String errorCustomMessage,
                                       DisplayTypeCode displayType, String customMessage,
                                       String logMessage, Throwable cause) {
        super(logMessage != null ? logMessage : "Application Exception", cause);
        this.errorCode = errorCode;
        this.errorCustomMessage = errorCustomMessage != null ? errorCustomMessage : (errorCode != null ? errorCode.getMessage() : "");
        this.displayType = displayType != null ? displayType : (errorCode != null ? errorCode.getDisplayType() : DisplayTypeCode.NONE);
        this.customMessage = customMessage != null ? customMessage : "실패";
        this.logMessage = logMessage != null ? logMessage : (errorCode != null ? errorCode.getMessage() : "");
        this.context = new HashMap<>();
    }

    /**
     * 컨텍스트 데이터 추가 (선택적 사용)
     */
    public NovaApplicationException addContext(String key, Object value) {
        this.context.put(key, value);
        return this;
    }

}
