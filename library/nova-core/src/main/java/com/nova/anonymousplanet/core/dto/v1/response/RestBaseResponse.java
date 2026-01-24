package com.nova.anonymousplanet.core.dto.v1.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nova.anonymousplanet.core.dto.v1.serializer.EmptyObjectSerializer;
import com.nova.anonymousplanet.core.util.ClientUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestBaseResponse {
    private boolean isSuccess; // 성공 여부 (true:성공, false:실패)
    private String message;    // 메시지
    private String traceId;    // 에러 추척 id Gateway에서 생성
    private String requestId;  // 요청 ID 각 서비스에서 생성(미사용)
    private String path;


    @JsonSerialize(nullsUsing = EmptyObjectSerializer.class)
    private ErrorSet error;    // Error 정보

    private LocalDateTime timestamp; // 반환 시간

    protected RestBaseResponse(boolean isSuccess, String message, ErrorSet error) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.traceId = MDC.get("traceId");
        this.requestId = MDC.get("requestId");
        this.path = ClientUtils.getCurrentRequestUri();
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
