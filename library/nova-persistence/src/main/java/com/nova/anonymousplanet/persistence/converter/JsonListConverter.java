package com.nova.anonymousplanet.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.persistence.converter
 * fileName : JsonListConverter
 * author : Jinhong Min
 * date : 2026-02-04
 * description : JSONArray형태의 문자열 데이터를 List<String>으로 변환
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-04      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
@Converter
public class JsonListConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("[nova-persistence] JSON writing error for List<String>", e);
            return "[]";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            log.error("[nova-persistence]JSON reading error for List<String>", e);
            return new ArrayList<>();
        }
    }
}
