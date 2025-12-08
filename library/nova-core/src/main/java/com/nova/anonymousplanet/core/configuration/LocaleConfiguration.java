package com.nova.anonymousplanet.core.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration
 * fileName : LocaleConfiguration
 * author : Jinhong Min
 * date : 2025-12-02
 * description : 언어 설정
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-02      Jinhong Min      최초 생성
 * ==============================================
 */

@Configuration
public class LocaleConfiguration {

    @Bean
    public LocaleResolver localeResolver() {
        NovaLocaleResolver resolver = new NovaLocaleResolver();
        resolver.setDefaultLocale(Locale.KOREA);
        return resolver;
    }

    /**
     * MessageSource는 국제화(i18n) 메시지를 읽기 위한 스프링 Bean이다.
     * messages.properties, messages_ko.properties 등에서 메시지를 로드하여
     * Locale에 맞는 문구를 반환한다.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();

        // 메시지를 찾을 수 없을 경우, 메시지 코드 자체를 반환
        ms.setUseCodeAsDefaultMessage(true);
        // 메시지 파일의 기본 경로 설정
        ms.setBasename("classpath:i18n/messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }


    public static class NovaLocaleResolver extends AcceptHeaderLocaleResolver {
        private static final String CUSTOM_HEADER = "N-Lang";

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String lang = request.getHeader(CUSTOM_HEADER);

            if (lang != null && !lang.isBlank()) {
                return Locale.forLanguageTag(lang);
            }

            return super.resolveLocale(request);
        }
    }
}
