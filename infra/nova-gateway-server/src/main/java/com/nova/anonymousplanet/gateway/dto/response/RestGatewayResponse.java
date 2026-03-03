package com.nova.anonymousplanet.gateway.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Deprecated
public record RestGatewayResponse(
        boolean isSuccess,
        String message,
        String traceId,
        String requestId,
        String path,
        GatewayErrorSet error,
        LocalDateTime timestamp
) implements Serializable {

    @Builder
    public RestGatewayResponse {
        timestamp = LocalDateTime.now();
    }

    public static RestGatewayResponse error(String path, String traceId, GatewayErrorSet error) {
        return RestGatewayResponse.builder()
                .isSuccess(false)
                .message("실패")
                .path(path)
                .traceId(traceId)
                .requestId(traceId) // Gateway에서는 traceId와 requestId 값이 같다
                .error(error)
                .build();
    }

    public record GatewayErrorSet(
            String code,
            String titleMessage,
            String detailMessage
    ) {}
}