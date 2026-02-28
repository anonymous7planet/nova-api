package com.nova.anonymousplanet.core.model.response;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.model.response
 * fileName : NovaError
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */
public record NovaErrorResponse(
        String code,                        // 상세 에러 코드 (ex: 001, 404)
        String message,                     // 사용자 노출 메시지
        DisplayTypeCode displayType,        // UI 노출 전략
        List<Violation> validationErrors    // 유효성 검사 실패 상세 목록
) {

    /**
     * Compact Constructor: validationErrors가 null일 경우 빈 리스트로 초기화
     */
    public NovaErrorResponse {
        if (validationErrors == null) {
            validationErrors = Collections.emptyList();
        }
    }

    /**
     * 기본 비즈니스 에러 생성 (검증 오류 없음)
     */
    public static NovaErrorResponse of(ErrorCode errorCode, String message, DisplayTypeCode displayType, List<Violation> validationErrors) {
        return new NovaErrorResponse(errorCode.getFullCode(), message, displayType, validationErrors);
    }

    public static NovaErrorResponse of(ErrorCode errorCode, String message, DisplayTypeCode displayType) {
        return of(errorCode, message, displayType, Collections.emptyList());
    }

    public static NovaErrorResponse of(ErrorCode errorCode,  DisplayTypeCode displayType) {
        return of(errorCode, errorCode.getMessage(), displayType);
    }

    public static NovaErrorResponse of(ErrorCode errorCode) {
        return of(errorCode, errorCode.getDisplayType());
    }
    /**
     * Spring Validation(BindingResult) 기반의 검증 에러 생성
     */
    public static NovaErrorResponse from(ErrorCode errorCode, String message, DisplayTypeCode displayType, BindingResult bindingResult) {
        if(bindingResult == null) {
            return of(errorCode, message, displayType);
        }
        List<Violation> violations = bindingResult.getFieldErrors().stream()
                .map(error -> new Violation(
                        error.getField(),
                        error.getDefaultMessage(),
                        String.valueOf(error.getRejectedValue())
                ))
                .toList();
        return of(errorCode, message, displayType, violations);
    }

    public static NovaErrorResponse from(String message, BindingResult bindingResult) {
        return from(CommonErrorCode.INVALID_INPUT_VALUE, message, DisplayTypeCode.ALERT, bindingResult);
    }


    /**
     * 유효성 검사 실패 상세 정보를 담는 내부 record
     */
    public record Violation(
            String field,           // 에러 발생 필드명
            String message,         // 필드별 에러 메시지
            Object rejectedValue    // 거절된 입력 값
    ) {
    }
}
