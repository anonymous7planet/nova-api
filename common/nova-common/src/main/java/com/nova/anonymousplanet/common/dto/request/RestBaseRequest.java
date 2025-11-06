package com.nova.anonymousplanet.common.dto.request;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public abstract class RestBaseRequest {
    protected String requestId;
    protected LocalDateTime timestamp;
    protected String clientInfo;
    protected String path;

    public RestBaseRequest() {
        this.timestamp = LocalDateTime.now();
    }
}
