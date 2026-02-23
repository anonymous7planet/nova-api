package com.nova.anonymousplanet.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.configuration
 * fileName : XssWebMvcConfiguration
 * author : Jinhong Min
 * date : 2026-02-23
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-23      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
public class XssWebMvcConfiguration implements WebMvcConfigurer {

    private final ObjectMapper xssObjectMapper;

    // 수동 생성자를 통해 Qualifier를 확실하게 주입
    public XssWebMvcConfiguration(@Qualifier("xssObjectMapper") ObjectMapper xssObjectMapper) {
        this.xssObjectMapper = xssObjectMapper;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 기존 컨버터 중 Jackson 컨버터를 찾아 우리가 만든 xssObjectMapper로 교체합니다.
        converters.stream()
                .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .map(MappingJackson2HttpMessageConverter.class::cast)
                .forEach(converter -> converter.setObjectMapper(xssObjectMapper));
    }
}
