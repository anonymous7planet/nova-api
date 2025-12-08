package com.nova.anonymousplanet.core.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestSingleResponse<T> extends RestBaseResponse {
    private T data;

    protected RestSingleResponse(T data, boolean isSuccess, String message) {
        super(isSuccess, message, null);
        this.data = data;
    }

    public static <T> RestSingleResponse<T> success(T data, String message) {
        return new RestSingleResponse<T>(data, true, message);
    }

    public static <T> RestSingleResponse<T> success(T data) {
        return new RestSingleResponse<T>(data, true, "성공");
    }
}
