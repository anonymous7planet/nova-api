package com.nova.anonymousplanet.core.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nova.anonymousplanet.core.dto.v1.serializer.EmptyObjectSerializer;
import org.slf4j.MDC;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.model.response
 * fileName : NovaResponse
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */

public record NovaResponse<T>(
        boolean success,
        String message,
        @JsonSerialize(nullsUsing = EmptyObjectSerializer.class) // data가 null이면 {}
        T data,
        String traceId,
        String requestId,
        String path,
        @JsonSerialize(nullsUsing = EmptyObjectSerializer.class) // error가 null이면 {}
        NovaErrorResponse error, // NovaError -> NovaErrorResponse 명칭 변경
        LocalDateTime timestamp
) implements Serializable {

    /**
     * Compact Constructor: MDC 정보 및 타임스탬프 자동 주입
     */
    public NovaResponse {
        traceId = MDC.get("traceId");
        requestId = MDC.get("requestId");
        path = MDC.get("path");
        timestamp = LocalDateTime.now();
    }

    // --- [성공 응답 팩토리 메서드] ---
    /**
     * 데이터와 메시지를 포함한 성공 응답
     */
    public static <T> NovaResponse<T> success(T data, String message) {
        return new NovaResponse<>(true, message, data, null, null, null, null, null);
    }

    /**
     * 데이터만 포함한 성공 응답 (기본 메시지 사용)
     */
    public static <T> NovaResponse<T> success(T data) {
        return success(data, "Operation successful");
    }

    /**
     * 데이터 없이 메시지만 포함한 성공 응답
     */
    public static <Object> NovaResponse<Object> success(String message) {
        return success(null, message);
    }

    // --- [실패 응답 팩토리 메서드] ---

    /**
     * 에러 정보를 포함한 실패 응답
     */
    public static <T> NovaResponse<T> fail(NovaErrorResponse errorResponse) {
        // 필드 순서는 사용자님의 NovaResponse 생성자 구조에 맞게 조정하세요.
        // 핵심은 마지막 T data 위치에 errorResponse를 넣는 것이 아니라,
        // 규격에 정의된 errorResponse 필드에 넣는 것입니다.
        return fail(errorResponse, "실패");
    }

    /**
     * 에러 정보와 커스텀 메시지를 포함한 실패 응답
     */
    public static <T> NovaResponse<T> fail(NovaErrorResponse errorResponse, String message) {
        return new NovaResponse<>(false, message, null, null, null, null, errorResponse, null);
    }
}
