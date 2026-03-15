package com.nova.anonymousplanet.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nova.anonymousplanet.core.util.JsonUtils;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.persistence.converter
 * fileName : AbstractJsonConverter
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
public abstract class AbstractJsonConverter<T> implements AttributeConverter<T, String> {

    // 직접 생성(new ObjectMapper)을 지우고 JsonUtils의 표준 매퍼를 참조
    protected ObjectMapper getObjectMapper() {
        return JsonUtils.getMapper();
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return getObjectMapper().writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("JSON Serialization Error: Object to String failed. Target: {}", attribute.getClass().getSimpleName(), e);
            throw new IllegalArgumentException("객체를 JSON 문자열로 변환하는데 실패했습니다.");
        }
    }

    // 복호화(String -> Object)는 타입 정보가 필요하므로 하위 클래스에서 구현
    @Override
    public abstract T convertToEntityAttribute(String dbData);
}