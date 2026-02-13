package com.nova.anonymousplanet.core.util;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : RecordMapper
 * author : Jinhong Min
 * date : 2026-02-07
 * description :
 * Record DTO를 Map으로 변환하는 유틸리티 (필터링 기능 추가)
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-07      Jinhong Min      최초 생성
 * ==============================================
 */
public class RecordMapper {
    private static final Map<Class<?>, RecordComponent[]> CACHE = new ConcurrentHashMap<>();

    private RecordMapper() {}

    /**
     * Record 객체를 Map으로 변환 (특정 필드 제외 가능)
     * @param record 변환할 객체
     * @param excludeKeys 제외하고 싶은 필드명들
     */
    public static Map<String, Object> toMap(Record record, String... excludeKeys) {
        if (record == null) return Collections.emptyMap();

        // 제외할 키들을 Set으로 변환 (검색 효율성 O(1))
        Set<String> excludeSet = Set.of(excludeKeys);

        RecordComponent[] components = CACHE.computeIfAbsent(record.getClass(), Class::getRecordComponents);

        return Arrays.stream(components)
                // 제외 목록에 포함되지 않은 컴포넌트만 필터링
                .filter(comp -> !excludeSet.contains(comp.getName()))
                .collect(Collectors.toUnmodifiableMap(
                        RecordComponent::getName,
                        comp -> {
                            try {
                                Object value = comp.getAccessor().invoke(record);
                                return value != null ? value : "";
                            } catch (Exception e) {
                                throw new RuntimeException("Record 변환 실패: " + comp.getName(), e);
                            }
                        }
                ));
    }
}
