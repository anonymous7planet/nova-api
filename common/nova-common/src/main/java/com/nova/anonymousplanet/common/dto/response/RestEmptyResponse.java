package com.nova.anonymousplanet.common.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestEmptyResponse extends RestBaseResponse implements Serializable {

    private RestEmptyResponse(boolean isSuccess, String message, ErrorSet error) {
       super(isSuccess, message, null, null, error);
    }

    public static RestEmptyResponse success(String message) {
        return new RestEmptyResponse(true, message, null);
    }

    public static RestEmptyResponse success() {
        return new RestEmptyResponse(true, "성공", null);
    }

    public static RestEmptyResponse fail(String message, ErrorSet error) {
        return new RestEmptyResponse(false, message, error);
    }
}
