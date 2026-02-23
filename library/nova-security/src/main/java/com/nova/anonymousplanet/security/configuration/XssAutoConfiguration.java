package com.nova.anonymousplanet.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.security.xss.HtmlCharacterEscapes;
import com.nova.anonymousplanet.security.xss.XssRaw;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.configuration
 * fileName : XssAutoConfiguration
 * author : Jinhong Min
 * date : 2026-02-23
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-23      Jinhong Min      최초 생성
 * ==============================================
 */

@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
public class XssAutoConfiguration {

    @Bean(name = "xssObjectMapper")
    public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 모든 API 응답에 XSS 이스케이프 적용
        objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return objectMapper;
    }

    @Bean(name = "rawObjectMapper")
    @XssRaw
    public ObjectMapper rawObjectMapper(Jackson2ObjectMapperBuilder builder) {
        // 서비스 간 통신용 원본 ObjectMapper
        return builder.createXmlMapper(false).build();
    }
}

//public record NoticeResponseEntity(
//        String title, // 자동 이스케이프 됨
//
//        @JsonRawValue // 에디터 HTML은 이스케이프를 건너뛰고 원본(Raw)으로 출력
//        String content
//) {}