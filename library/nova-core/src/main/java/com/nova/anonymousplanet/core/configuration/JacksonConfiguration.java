package com.nova.anonymousplanet.core.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration
 * fileName : JacksonConfig
 * author : Jinhong Min
 * date : 2026-03-10
 * description : TODO: ObjectMapper 통합 필요
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-10      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
public class JacksonConfiguration {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

//    @Bean
//    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false) // DTO에 없는 필드가 들어와도 에러 내지 않음 (유연성)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 날짜를 배열이 아닌 문자열로 포맷팅
                .serializationInclusion(JsonInclude.Include.NON_NULL) // null인 필드는 제외하고 전송
                .modules(new JavaTimeModule()) // LocalDateTime 지원
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)))
                .deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)))
                .build();
    }
}
