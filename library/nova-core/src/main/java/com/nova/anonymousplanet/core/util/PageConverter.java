package com.nova.anonymousplanet.core.util;

import com.nova.anonymousplanet.core.model.response.NovaPagingResponse;
import org.springframework.data.domain.Page;

import java.util.function.Function;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : PageConverter
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */
public class PageConverter {
    /**
     * JPA Page를 Core PagingResponse로 변환 (Entity -> DTO 변환 포함)
     */
    public static <E, D> NovaPagingResponse<D> toResponse(Page<E> page, Function<E, D> mapper) {
        return new NovaPagingResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}
