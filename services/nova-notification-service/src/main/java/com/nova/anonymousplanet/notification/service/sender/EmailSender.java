package com.nova.anonymousplanet.notification.service.sender;

import com.nova.anonymousplanet.notification.configuration.EmailProperties;
import com.nova.anonymousplanet.core.event.email.EmailAttachment;
import com.nova.anonymousplanet.notification.model.EmailPayload;
import com.nova.anonymousplanet.core.event.email.InlineImage;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Paths;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.service.sender
 * fileName : EmailSenderService
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
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "nova.notification.email", name = "enabled", havingValue = "true")
public class EmailSender {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    public void send(EmailPayload payload) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    msg,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8"
            );

            // 1. 기본 정보 설정
            helper.setFrom(emailProperties.getFrom());
            helper.setTo(payload.to());
            helper.setSubject(payload.subject());

            // 2. 본문 렌더링 (Thymeleaf)
            applyTemplate(helper, payload);

            // 3. 첨부파일 및 인라인 이미지
            attachFiles(helper, payload);
            attachImages(helper, payload);

            mailSender.send(msg);
            log.info("[EmailSenderService] Sent successfully to: {}", payload.to());

        } catch (Exception e) {
            log.error("[EmailSenderService] Failed to send to: {}. Error: {}", payload.to(), e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private void applyTemplate(MimeMessageHelper helper, EmailPayload payload) throws Exception {
        Context ctx = new Context();
        if (payload.variables() != null) {
            payload.variables().forEach(ctx::setVariable);
        }
        // EmailTemplateType 열거형에서 파일명을 가져오도록 설계됨
        String html = thymeleafTemplateEngine.process(payload.templateFileName(), ctx);
        helper.setText(html, true);
    }

    private void attachFiles(MimeMessageHelper helper, EmailPayload payload) throws Exception {
        if (payload.attachments() == null) return;

        for (EmailAttachment att : payload.attachments()) {
            // 용량 체크 (Fail-Fast)
            if (att.data().length > emailProperties.getMaxAttachmentSize()) {
                throw new IllegalArgumentException("Attachment too large: " + att.fileName());
            }
            DataSource ds = new ByteArrayDataSource(att.data(), att.contentType());
            helper.addAttachment(att.fileName(), ds);
        }
    }

    private void attachImages(MimeMessageHelper helper, EmailPayload payload) throws Exception {
        if (payload.inlineImages() == null) return;

        for (InlineImage img : payload.inlineImages()) {
            DataSource ds = loadInlineImage(img.fileName());
            helper.addInline(img.contentId(), ds);
        }
    }

    private DataSource loadInlineImage(String imageName) throws IOException {
        // 1. 외부 파일 시스템 우선 탐색
        if (emailProperties.getExternalImagePath() != null) {
            File file = Paths.get(emailProperties.getExternalImagePath(), imageName).toFile();
            if (file.exists()) {
                return new FileDataSource(file);
            }
        }

        // 2. Classpath fallback (JAR 환경 대응을 위해 getInputStream 사용)
        ClassPathResource resource = new ClassPathResource("email/images/" + imageName);
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                byte[] data = is.readAllBytes();
                // 스트림을 통해 Content-Type 추측 (JAR 내부 파일 대응)
                String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(data));
                return new ByteArrayDataSource(data, contentType != null ? contentType : "image/png");
            }
        }

        throw new FileNotFoundException("Nova Email: Inline image not found -> " + imageName);
    }
}
