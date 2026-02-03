package com.nova.anonymousplanet.notification.provider;

import com.nova.anonymousplanet.notification.configuration.EmailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.provider
 * fileName : EmailTemplateEngineProvider
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
@Component
@ConditionalOnProperty(prefix = "nova.email", name = "enabled", havingValue = "true")
public class EmailTemplateEngineProvider {

    private final EmailProperties emailProperties;

    private TemplateEngine templateEngine;

    public EmailTemplateEngineProvider(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    /**
     * 템플릿 엔진을 지연 초기화(lazy init) 방식으로 제공.
     * 외부 템플릿 경로 > classpath 템플릿 순으로 resolver가 적용됨.
     */
    public TemplateEngine getTemplateEngine() {

        if (templateEngine != null) {
            return templateEngine;
        }

        log.info("[EmailTemplate] Initializing Thymeleaf template engine...");

//        TemplateEngine engine = new TemplateEngine();
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);

        // ----------------------------------------------------
        // 1. 외부 파일 템플릿 (옵션)
        // ----------------------------------------------------
        if (emailProperties.getExternalTemplatePath() != null &&
            !emailProperties.getExternalTemplatePath().isBlank()) {

            String prefix = normalizePath(emailProperties.getExternalTemplatePath());

            FileTemplateResolver fileResolver = new FileTemplateResolver();
            fileResolver.setPrefix(prefix);
            fileResolver.setSuffix(emailProperties.getSuffix()); // 확장자를 파일명에 포함하여 사용 가능
            fileResolver.setTemplateMode("HTML");
            fileResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
            fileResolver.setOrder(1);
            fileResolver.setCacheable(false);

            engine.addTemplateResolver(fileResolver);

            log.info("[EmailTemplate] File resolver activated. path={}", prefix);
        }

        // ----------------------------------------------------
        // 2. Classpath 템플릿 (fallback)
        // ----------------------------------------------------
        ClassLoaderTemplateResolver classResolver = new ClassLoaderTemplateResolver();
        classResolver.setPrefix("templates/email/");
        classResolver.setSuffix(emailProperties.getSuffix()); // 기본은 html 확장자 사용
        classResolver.setTemplateMode("HTML");
        classResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        classResolver.setOrder(2);
        classResolver.setCacheable(false);

        engine.addTemplateResolver(classResolver);
        log.info("[EmailTemplate] Classpath resolver configured. (templates/email/)");

        this.templateEngine = engine;
        return this.templateEngine;
    }


    /**
     * 외부 템플릿 경로 prefix를 자동으로 보정.
     * 예: "/opt/mail" → "/opt/mail/"
     */
    private String normalizePath(String path) {
        String result = path;
        String sep = System.getProperty("file.separator");

        if (!result.endsWith("/") && !result.endsWith("\\")) {
            result += sep;
        }

        return result;
    }
}
