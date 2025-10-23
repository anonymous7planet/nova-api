package com.nova.anonymousplanet.common.dto.request;


import lombok.Getter;

@Getter
public class PaginationRequest extends BaseRequest {
    private final int page = 1;
    private final int size = 20;
    private String sortBy;
    private String direction;
}
