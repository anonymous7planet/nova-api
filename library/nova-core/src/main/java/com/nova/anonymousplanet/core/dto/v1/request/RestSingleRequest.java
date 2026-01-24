package com.nova.anonymousplanet.core.dto.v1.request;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.dto.request
  fileName : RestSingleRequest
  author : Jinhong Min
  date : 2025-10-28
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-28      Jinhong Min      최초 생성
  ==============================================
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestSingleRequest<T> extends RestBaseRequest {

    @Valid
    private T data;

    private RestSingleRequest(String requestId, String path, String clientInfo, T data) {
        super.requestId = requestId;
        super.path = path;
        super.clientInfo = clientInfo;
        this.data = data;
    }


    public static <T> RestSingleRequest<T> of(String requestId, String path, T data) {
        return new RestSingleRequest<>(requestId, path, "", data);
    }
}
