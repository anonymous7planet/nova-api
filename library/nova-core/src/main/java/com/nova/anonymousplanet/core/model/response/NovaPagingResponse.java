package com.nova.anonymousplanet.core.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.model.response
 * fileName : PagingResponse
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */

public record NovaPagingResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) implements Serializable {
    public static <T> NovaPagingResponse<T> empty() {
        return new NovaPagingResponse<>(List.of(), 0, 0, 0, 0, false);
    }
}