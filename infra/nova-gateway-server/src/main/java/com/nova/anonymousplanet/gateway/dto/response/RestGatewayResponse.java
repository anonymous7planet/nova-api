package com.nova.anonymousplanet.gateway.dto.response;

import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.io.Serializable;
import java.time.LocalDateTime;

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
                .requestId(traceId) // Gateway에서는 요청 ID를 traceId와 동일시하거나 별도 추출
                .error(error)
                .build();
    }

    public record GatewayErrorSet(
            String code,
            String titleMessage,
            String detailMessage
    ) {}
}