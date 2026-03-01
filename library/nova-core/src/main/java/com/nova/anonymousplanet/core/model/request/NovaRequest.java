package com.nova.anonymousplanet.core.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.model.request
 * fileName : NovaRequest
 * author : Jinhong Min
 * date : 2026-02-28
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-28      Jinhong Min      최초 생성
 * ==============================================
 */
public record NovaRequest<T>(
        @Valid
        @NotNull(message = "요청 헤더는 필수입니다.")
        RequestHeader header,

        @Valid
        @NotNull(message = "요청 본문(body)은 필수입니다.")
        T body
) {
    /**
     * 공통 요청 메타데이터
     */
    public record RequestHeader(
            @NotNull(message = "기기 정보는 필수입니다.")
            String deviceType,   // IOS, ANDROID, WEB 등

            @NotNull(message = "기기 정보는 필수입니다.")
            String deviceId,   // IOS, ANDROID, WEB 등

            @NotNull(message = "앱 버전은 필수입니다.")
            String appVersion,   // 서비스 추적용 버전

            LocalDateTime requestTime // 요청 생성 시각
    ) {
        // Compact Constructor: 기본값 설정
        public RequestHeader {
            if (requestTime == null) {
                requestTime = LocalDateTime.now();
            }
        }
    }
}
