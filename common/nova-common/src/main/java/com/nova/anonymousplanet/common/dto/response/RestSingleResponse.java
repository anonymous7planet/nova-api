package com.nova.anonymousplanet.common.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestSingleResponse<T> extends RestBaseResponse {
    private T data;

    protected RestSingleResponse(T data, boolean isSuccess, String message, String requestId, String path) {
        super(isSuccess, message, requestId, path, null);
        this.data = data;
    }

    public static <T> RestSingleResponse<T> success(T data, String message, String requestId, String path) {
        return new RestSingleResponse<T>(data, true, message, requestId, path);
    }

    public static <T> RestSingleResponse<T> success(T data, String requestId, String path) {
        return new RestSingleResponse<T>(data, true, "성공", requestId, path);
    }
}
