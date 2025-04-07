package com.nova.anonymousplanet.common.dto.request;


import lombok.Getter;

@Getter
public class PaginationRequest extends BaseRequest {
    private int page = 1;
    private int size = 20;
    private String sortBy;
    private String direction;
}
