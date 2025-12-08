package com.nova.anonymousplanet.core.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestListResponse<T> extends RestBaseResponse implements Serializable {
    private PageInfo pageInfo;
    private List<T> data;

    protected RestListResponse(boolean isSuccess, String message, PageInfo pageInfo, List<T> data) {
        super(isSuccess, message, null);
        this.pageInfo = pageInfo;
        this.data = data;
    }

    public static <T> RestListResponse<T> success(String message, PageInfo pageInfo, List<T> data) {
        return new RestListResponse<>(true, message, pageInfo, data);
    }

    public static <T> RestListResponse<T> success(PageInfo pageInfo, List<T> data) {
        return new RestListResponse<>(true, "성공", pageInfo, data);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PageInfo {
        private Long totalElements;
        private int totalPages;
        private int currentPage;
        private int pageSize;
        private boolean hasNext;
        private boolean hasPrevious;

        @Builder
        public PageInfo(Long totalElements, int totalPages, int currentPage, int pageSize, boolean hasNext, boolean hasPrevious) {
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.hasNext = hasNext;
            this.hasPrevious = hasPrevious;
        }
    }
}
