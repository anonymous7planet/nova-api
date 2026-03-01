package com.nova.anonymousplanet.web.configuration;

import com.nova.anonymousplanet.web.configuration.converter.StringToEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.web.configuration
 * fileName : WebConfiguration
 * author : Jinhong Min
 * date : 2026-03-01
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-01      Jinhong Min      최초 생성
 * ==============================================
 */

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // String -> Enum 변환기 등록
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }

}
