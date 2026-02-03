package com.nova.anonymousplanet.notification.service;

import com.nova.anonymousplanet.notification.configuration.EmailProperties;
import com.nova.anonymousplanet.notification.model.EmailAttachment;
import com.nova.anonymousplanet.notification.model.EmailPayload;
import com.nova.anonymousplanet.notification.model.InlineImage;
import com.nova.anonymousplanet.notification.provider.EmailTemplateEngineProvider;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.service.sender
 * fileName : EmailSenderService
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
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "nova.email", name = "enabled", havingValue = "true")
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final EmailTemplateEngineProvider templateProvider;

    public void send(EmailPayload payload) {

        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                msg,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                "UTF-8"
            );

            setFrom(helper);
            helper.setTo(payload.to());
            helper.setSubject(payload.subject());

            // template 적용
            applyTemplate(helper, payload);

            // 첨부파일
            attachFiles(helper, payload);

            // 첨부이미지
            attachImages(helper, payload);

            mailSender.send(msg);

            log.info("[Email] Sent successfully: {}", payload.to());

        } catch (Exception e) {
            log.error("[Email] Failed: {} / {}", payload.to(), e.getMessage(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private void setFrom(MimeMessageHelper helper) throws Exception {
        helper.setFrom(emailProperties.getFrom());
    }

    private void applyTemplate(MimeMessageHelper helper, EmailPayload payload) {
        try {
            Context ctx = new Context();
            payload.variables().forEach(ctx::setVariable);

            String html = templateProvider.getTemplateEngine()
                .process(payload.templateType().getFileName(), ctx);

            helper.setText(html, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void attachFiles(MimeMessageHelper helper, EmailPayload payload) throws Exception {
        if (payload.attachments() == null) return;

        for (EmailAttachment att : payload.attachments()) {
            if (att.data().length > emailProperties.getMaxAttachmentSize()) {
                throw new IllegalArgumentException("첨부파일 용량 초과: " + att.fileName());
            }
            DataSource ds = new ByteArrayDataSource(att.data(), att.contentType());
            helper.addAttachment(att.fileName(), ds);
        }
    }

    private void attachImages(MimeMessageHelper helper, EmailPayload payload) throws Exception {
        if (payload.inlineImages() == null) return;


        // template파일에는 ${'cid:contentId'} 로 표현
        for (InlineImage inlineImage : payload.inlineImages()) {
            String imageName = inlineImage.fileName();   // 예: logo.png
            String contentId = inlineImage.contentId();  // 예: logoImage

            DataSource dataSource = loadInlineImage(imageName);
            helper.addInline(contentId, dataSource);
        }
    }

    private DataSource loadInlineImage(String imageName) throws IOException {

        // 1. 외부 경로 우선
        if (emailProperties.getExternalImagePath() != null) {

            File file = Paths.get(emailProperties.getExternalImagePath(), imageName).toFile();

            if (file.exists()) {
                return new FileDataSource(file); // DataSource로 바로 사용 가능
            }
        }

        // 2. classpath 이미지 fallback
        ClassPathResource resource = new ClassPathResource("email/images/" + imageName);

        if (resource.exists()) {
            return new ByteArrayDataSource(resource.getInputStream(), Files.probeContentType(resource.getFile().toPath()));
        }

        throw new FileNotFoundException("Inline image not found: " + imageName);
    }




}
