package com.nova.anonymousplanet.common.dto.response;

import com.nova.anonymousplanet.common.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;


@Getter
public class ErrorSet {

    private final String code;
    private final String titleMessage;
    private final String detailMessage;
    private final List<ValidationError> validationErrors;

    @Builder
    public ErrorSet(ErrorCode errorCode, String titleMessage, String detailMessage, List<ValidationError> validationErrors) {
        this.code = errorCode.getCode();
        this.titleMessage = titleMessage;
        this.detailMessage = detailMessage;
        this.validationErrors = (validationErrors != null) ? validationErrors : Collections.emptyList();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ValidationError {
        private String field;
        private String message;
        private Object rejectedValue;

        @Builder
        public ValidationError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }
    }
}
