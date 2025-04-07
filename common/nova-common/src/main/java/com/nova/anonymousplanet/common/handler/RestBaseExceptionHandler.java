package com.nova.anonymousplanet.common.handler;


import com.nova.anonymousplanet.common.constant.ErrorCode;
import com.nova.anonymousplanet.common.dto.response.ErrorSet;
import com.nova.anonymousplanet.common.dto.response.RestEmptyResponse;
import com.nova.anonymousplanet.common.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;

/**
 * 기본 Exception 적용 각 서비스에서 해당 클래스 상속 받아서 사용
 */
@Slf4j
public abstract class RestBaseExceptionHandler {

    /**
     * @Valid, @Validated에서 발생한 예외 처리
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestEmptyResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorSet.ValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorSet.ValidationError
                        .builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue())
                        .build())
                .toList();

        log.warn("[Validation Error] URI: {}, Errors: {}", request.getRequestURI(), validationErrors);

        return buildErrorSet(request, ErrorCode.VALIDATION, "입력 값을 확인 해주세요.", validationErrors);
    }


    /**
     * javax.validation.ConstraintViolationException 예외 처리
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestEmptyResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        List<ErrorSet.ValidationError> validationErrors = ex.getConstraintViolations().stream()
                .map(violation -> ErrorSet.ValidationError
                        .builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .rejectedValue(violation.getInvalidValue())
                        .build())
                .toList();

        log.warn("[ConstraintViolation Error] URI: {}, Errors: {}", request.getRequestURI(), validationErrors);

        return buildErrorSet(request, ErrorCode.VALIDATION, "입력 값을 확인 해주세요.", validationErrors);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<RestEmptyResponse> handleApplicationException(ApplicationException ex, HttpServletRequest request) {
        log.error("[ApplicationException] URI: {}, ErrorCode: {}, Message: {}",
                request.getRequestURI(), ex.getErrorCode().getCode(), ex.getMessage());

        return buildErrorSet(request, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestEmptyResponse> handleException(Exception ex, HttpServletRequest request) {

        log.error("[Unexpected Error] URI: {}, Message: {}", request.getRequestURI(), ex.getMessage(), ex);

        return buildErrorSet(request, ErrorCode.INTERNAL_SERVER_ERROR, "예상하지 못한 에러가 발생했습니다.", Collections.emptyList());
    }


    private ResponseEntity<RestEmptyResponse> buildErrorSet(HttpServletRequest request, ErrorCode errorCode, String message, List<ErrorSet.ValidationError> validationErrors) {
        return buildErrorSet(request, errorCode, message, errorCode.getTitleMessage(), errorCode.getDetailMessage(), validationErrors);
    }

    private ResponseEntity<RestEmptyResponse> buildErrorSet(HttpServletRequest request, ApplicationException ex) {
        return buildErrorSet(request, ex.getErrorCode(), ex.getMessage(), ex.getTitleMessage(), ex.getDetailMessage(), Collections.emptyList());
    }

    private ResponseEntity<RestEmptyResponse> buildErrorSet(HttpServletRequest request, ErrorCode errorCode, String message, String titleMessage, String detailMessage, List<ErrorSet.ValidationError> validationErrors) {
        ErrorSet error = ErrorSet
                .builder()
                .errorCode(errorCode)
                .titleMessage(titleMessage)
                .detailMessage(detailMessage)
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(
                RestEmptyResponse.fail(message, error)
        );
    }
}
