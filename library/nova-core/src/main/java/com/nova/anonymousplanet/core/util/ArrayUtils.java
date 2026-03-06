package com.nova.anonymousplanet.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : ArrayUtils
 * author : Jinhong Min
 * date : 2026-03-05
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-05      Jinhong Min      최초 생성
 * ==============================================
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayUtils {

    /**
     * 여러 개의 문자열 배열을 하나로 결합하고 중복을 제거합니다.
     * @param base 기본이 되는 배열 (예: 공통 화이트리스트)
     * @param additions 추가할 배열들 혹은 개별 요소들
     * @return 결합 및 중복 제거된 통합 배열
     */
    public static String[] concatDistinct(String[] base, String... additions) {
        if (base == null) return additions;
        if (additions == null) return base;

        return Stream.concat(Stream.of(base), Stream.of(additions))
                .distinct()
                .toArray(String[]::new);
    }

}
