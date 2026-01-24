package com.nova.anonymousplanet.core.dto.v1.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nova.anonymousplanet.core.dto.v1.serializer.EmptyObjectSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestSingleResponse<T> extends RestBaseResponse {

    @JsonSerialize(nullsUsing = EmptyObjectSerializer.class)
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