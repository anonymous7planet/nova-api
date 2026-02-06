package com.nova.anonymousplanet.notification.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.configuration
 * fileName : EmailConfiguration
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "nova.notification.email", name = "enabled", havingValue = "true")
public class EmailConfiguration {

    private final EmailProperties emailProperties;


    /**
     * SpringTemplateEngine을 Bean으로 등록하여 주입 가능하게 설정.
     * 초기화 시점에 모든 Resolver를 설정하므로 Thread-Safe함.
     */
    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);

        // Resolver들을 체이닝하여 추가
        Set<ITemplateResolver> resolvers = new LinkedHashSet<>();

        // 1. 외부 경로 설정이 있다면 최우선 순위로 추가
        if (StringUtils.hasText(emailProperties.getExternalTemplatePath())) {
            resolvers.add(externalTemplateResolver());
        }

        // 2. 기본 클래스패스 리졸버 추가
        resolvers.add(classPathResolver());

        engine.setTemplateResolvers(resolvers);
        log.info("Project Nova: Email TemplateEngine initialized with {} resolvers", resolvers.size());

        return engine;
    }

    /**
     * 1순위: 외부 파일 시스템 리졸버 (설정값이 있을 때만 작동하도록 Order 부여 가능)
     */
    private ITemplateResolver externalTemplateResolver() {
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(normalizePath(emailProperties.getExternalTemplatePath()));
        resolver.setSuffix(emailProperties.getSuffix());
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setOrder(1);
//        resolver.setCacheable(emailProperties.isTemplateCache());
        resolver.setCacheable(false);
        resolver.setCheckExistence(true); // 파일 부재 시 다음 리졸버로 Pass
        return resolver;
    }

    /**
     * 2순위: 클래스패스 리졸버 (기본 내장 템플릿)
     */
    private ITemplateResolver classPathResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/email/"); // Nova 표준 경로
        resolver.setSuffix(emailProperties.getSuffix());
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setOrder(2);
        resolver.setCacheable(emailProperties.isTemplateCache());
        return resolver;
    }

    /**
     * 외부 템플릿 경로 prefix를 자동으로 보정.
     * 예: "/opt/mail" → "/opt/mail/"
     */
    private String normalizePath(String path) {
        if (!StringUtils.hasText(path)) return path;
        String sep = File.separator;
        return path.endsWith(sep) ? path : path + sep;
    }



    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(emailProperties.getHost());
        sender.setPort(emailProperties.getPort());
        sender.setUsername(emailProperties.getUsername());
        sender.setPassword(emailProperties.getPassword());

        Properties p = new Properties();
        p.put("mail.smtp.auth", true);
        p.put("mail.smtp.starttls.enable", true);
        sender.setJavaMailProperties(p);

        return sender;
    }
}
