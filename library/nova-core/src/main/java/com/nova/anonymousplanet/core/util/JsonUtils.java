package com.nova.anonymousplanet.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : JsonUtils
 * author : Jinhong Min
 * date : 2026-03-15
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-15      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtils {

    private final ObjectMapper objectMapper;
    private static ObjectMapper staticMapper;

    @PostConstruct
    private void init() {
        staticMapper = objectMapper;
    }

    /**
     * 객체를 JSON 문자열로 변환
     */
    public static String toJson(Object obj) {
        try {
            return staticMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON Serialization Error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * JSON 문자열을 객체로 변환
     */
    public static <T> Optional<T> fromJson(String json, Class<T> clazz) {
        try {
            return Optional.ofNullable(staticMapper.readValue(json, clazz));
        } catch (JsonProcessingException e) {
            log.error("JSON Deserialization Error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static ObjectMapper getMapper() {
        if (staticMapper == null) {
            log.warn("ObjectMapper is not yet initialized. Returning a new instance as fallback.");
            return new ObjectMapper(); // 극단적인 방어 코드
        }
        return staticMapper;
    }
}
