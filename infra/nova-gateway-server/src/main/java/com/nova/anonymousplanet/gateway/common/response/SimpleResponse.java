package com.nova.anonymousplanet.gateway.common.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleResponse implements Serializable {
    private boolean isSuccess;
    private String message;
    private String requestId;
    private SimpleErrorSet error;
    private LocalDateTime timestamp;

    @Builder
    public SimpleResponse(boolean isSuccess, String message, String requestId, SimpleErrorSet error) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.requestId = requestId;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
    public static SimpleResponse success(String message, String requestId) {
        return new SimpleResponse(true, message, requestId, null);
    }

    public static SimpleResponse success(String requestId) {
        return success("성공", requestId);
    }

    public static SimpleResponse error(String message, String requestId, SimpleErrorSet error) {
        return new SimpleResponse(false, message, requestId, error);
    }

    public static SimpleResponse error(String requestId, SimpleErrorSet error) {
       return new SimpleResponse(false, "실패", requestId, error);
    }


    @Getter
    public static class SimpleErrorSet {
        private final String path;
        private final String code;
        private final String detailMessage;

        @Builder
        public SimpleErrorSet(String path, String code, String detailMessage) {
            this.path = path;
            this.code = code;
            this.detailMessage = detailMessage;
        }
    }
}