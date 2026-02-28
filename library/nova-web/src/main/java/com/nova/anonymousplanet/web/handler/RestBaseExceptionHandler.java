package com.nova.anonymousplanet.web.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;
import com.nova.anonymousplanet.core.exception.category.BusinessException;
import com.nova.anonymousplanet.core.exception.category.NotFoundException;
import com.nova.anonymousplanet.core.exception.category.SecurityAuthException;
import com.nova.anonymousplanet.core.exception.category.ServerException;
import com.nova.anonymousplanet.core.exception.domain.common.InvalidEnumCodeException;
import com.nova.anonymousplanet.core.model.response.NovaErrorResponse;
import com.nova.anonymousplanet.core.model.response.NovaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.web.handler
 * fileName : GlobalExceptionHandler
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
public class RestBaseExceptionHandler {

    /**
     * [Case 1] 프로젝트 표준 예외 처리
     */
    @ExceptionHandler(NovaApplicationException.class)
    public ResponseEntity<NovaResponse<Void>> handleNovaException(NovaApplicationException ex) {
        return createResponse(ex, null);
    }

    /**
     * [Case 2] 잘못된 Enum값 예외 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<NovaResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("[handleHttpMessageNotReadableException] ", ex);
        // 1. Jackson이 감싼 예외 내부에서 우리가 던진 BusinessException(또는 InvalidEnumCodeException)을 찾음
        Throwable rootCause = ex.getCause(); // Spring 6+ / Jackson 지원 유틸

        // 1. BusinessException(InvalidEnumCodeException)이 Jackson에 감싸진 경우
        if (rootCause instanceof ValueInstantiationException vie) {
            String fieldName = extractLastFieldName(vie.getPath()); // 필드명 추출

            if (vie.getCause() instanceof InvalidEnumCodeException iec) {
                String refinedMessage = String.format("[%s]%s",
                        fieldName, iec.getErrorCustomMessage());

                return ResponseEntity
                        .status(iec.getErrorCode().getStatus())
                        .body(NovaResponse.fail(NovaErrorResponse.of(iec.getErrorCode(), refinedMessage, iec.getDisplayType())))
                        ;
            }
        }

        // 2. 진짜 JSON 문법이 틀렸거나 파싱이 아예 불안정한 경우
        log.error("JSON 파싱 에러: {}", ex.getMessage());
        var errorCode = CommonErrorCode.INVALID_INPUT_VALUE;

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(NovaResponse.fail(NovaErrorResponse.of(errorCode)));
    }

    /**
     * [Case 3] Bean Validation 예외
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<NovaResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("[handleValidationException] ", ex);

        // 1. 공통 에러 코드 정의 (INVALID_INPUT_VALUE 사용)
        var errorCode = CommonErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(NovaResponse.fail(NovaErrorResponse.from(errorCode, errorCode.getMessage(), errorCode.getDisplayType(), ex.getBindingResult())));
    }

    /**
     * [Case 4] 그 외 예상치 못한 예외
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<NovaResponse<Void>> handleUnexpectedException(Exception ex) {
        log.error("[handleUnexpectedException] ", ex);
        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(NovaResponse.fail(NovaErrorResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR)));
    }

    // --- 내부 헬퍼 메서드 ---
    private void logError(NovaApplicationException ex) {
        // 마지막 인자에 'ex'를 그대로 넘겨야 Stack Trace가 출력됩니다.
        log.error("[{} ERROR] code: {}, logMessage: {}, context: {}",
                ex.getClass().getSimpleName(),
                ex.getErrorCode().getFullCode(),
                ex.getLogMessage(),
                ex.getContext(),
                ex // <--- 핵심: {} 자리에 들어가는 인자가 아니라, 마지막 별도 인자로 전달
        );
    }

    /**
     * Jackson의 Reference 경로에서 실제 필드명을 추출 (e.g., "data.gender")
     */
    private String extractLastFieldName(List<JsonMappingException.Reference> path) {
        if (path == null || path.isEmpty()) return "unknown";

        // 리스트의 마지막 요소를 가져옴
        JsonMappingException.Reference lastReference = path.get(path.size() - 1);

        // 필드명이 있으면 필드명 반환, 없으면 인덱스(리스트인 경우) 반환
        if (lastReference.getFieldName() != null) {
            return lastReference.getFieldName();
        }

        if (lastReference.getIndex() >= 0) {
            return String.valueOf(lastReference.getIndex());
        }

        return "unknown";
    }
    private String extractFieldName(List<JsonMappingException.Reference> path) {
        if (path == null || path.isEmpty()) return "unknown";

        return path.stream()
                .map(ref -> {
                    if (ref.getFieldName() != null) return ref.getFieldName();
                    if (ref.getIndex() >= 0) return String.valueOf(ref.getIndex());
                    return "?";
                })
                .collect(Collectors.joining("."));
    }

    private ResponseEntity<NovaResponse<Void>> createResponse(NovaApplicationException ex, BindingResult bindingResult) {
        logError(ex);
        ErrorCode errorCode = ex.getErrorCode(); // 예: CommonErrorCode.INVALID_ENUM_VALUE
        NovaErrorResponse errorResponse = NovaErrorResponse.from(errorCode, ex.getErrorCustomMessage(), ex.getDisplayType(), bindingResult);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(NovaResponse.fail(errorResponse, ex.getCustomMessage()));
    }

}
