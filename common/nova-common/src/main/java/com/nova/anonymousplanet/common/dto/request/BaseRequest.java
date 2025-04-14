package com.nova.anonymousplanet.common.dto.request;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseRequest {
    protected String requestId;
    protected LocalDateTime timestamp;
    protected String clientInfo;

    public BaseRequest() {
        this.timestamp = LocalDateTime.now();
    }
}
