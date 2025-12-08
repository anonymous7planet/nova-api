package com.nova.anonymousplanet.core.dto.response;

import com.nova.anonymousplanet.core.util.ClientUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestBaseResponse {
    private boolean isSuccess; // 성공 여부 (true:성공, false:실패)
    private String message;    // 메시지
    @Setter
    private String requestId;  // 요청 ID Gateway에서 생성
    @Setter
    private String path;
    private ErrorSet error;    // Error 정보
    private LocalDateTime timestamp; // 반환 시간

    protected RestBaseResponse(boolean isSuccess, String message, ErrorSet error) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.requestId = MDC.get("requestId");
        this.path = ClientUtils.getCurrentRequestUri();
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
