package com.nova.anonymousplanet.core.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.dto.request
  fileName : RestPageRequest
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
@AllArgsConstructor
@Builder
public class RestPageRequest<T> extends RestBaseRequest{

    @Valid
    private PageParam page;

    @Valid
    private T filter; // 검색/필터용 DTO (예: UserSearchCondition)

    public PageRequest toPageRequest() {
        Sort sort = Sort.by(page.getDirection().isAsc()
            ? Sort.Order.asc(page.getSortBy())
            : Sort.Order.desc(page.getSortBy()));
        return PageRequest.of(page.getPage(), page.getSize(), sort);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class PageParam {
        @Min(0)
        private int page = 0;

        @Min(1)
        @Max(100)
        private int size = 20;

        private String sortBy = "createdAt";

        private SortDirection direction = SortDirection.DESC;
    }

    public enum SortDirection {
        ASC, DESC;

        public boolean isAsc() {
            return this == ASC;
        }
    }
}
