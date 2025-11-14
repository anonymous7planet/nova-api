package com.nova.anonymousplanet.gateway.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestGatewayResponse implements Serializable {
    private boolean isSuccess;
    private String message;
    private String requestId;
    private GatewayErrorSet error;
    private LocalDateTime timestamp;

    @Builder
    public RestGatewayResponse(boolean isSuccess, String message, String requestId, GatewayErrorSet error) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.requestId = requestId;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
    public static RestGatewayResponse success(String message, String requestId) {
        return new RestGatewayResponse(true, message, requestId, null);
    }

    public static RestGatewayResponse success(String requestId) {
        return success("성공", requestId);
    }

    public static RestGatewayResponse error(String message, String requestId, GatewayErrorSet error) {
        return new RestGatewayResponse(false, message, requestId, error);
    }

    public static RestGatewayResponse error(String requestId, GatewayErrorSet error) {
       return new RestGatewayResponse(false, "실패", requestId, error);
    }


    public record GatewayErrorSet(String path, String code, String detailMessage) {
    }
}