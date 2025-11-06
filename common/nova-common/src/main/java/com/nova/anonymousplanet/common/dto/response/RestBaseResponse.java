package com.nova.anonymousplanet.common.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    protected RestBaseResponse(boolean isSuccess, String message, String requestId, String path, ErrorSet error) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.requestId = requestId;
        this.path = path;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }


}
