package com.nova.anonymousplanet.common.dto.common.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseRequest {
    private String requestId;
    private LocalDateTime timestamp;
    private String clientInfo;

    public BaseRequest() {
        this.timestamp = LocalDateTime.now();
    }
}
