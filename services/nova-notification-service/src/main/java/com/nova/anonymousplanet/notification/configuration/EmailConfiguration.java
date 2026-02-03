package com.nova.anonymousplanet.notification.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

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

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "nova.email", name = "enabled", havingValue = "true")
public class EmailConfiguration {

    private final EmailProperties props;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(props.getHost());
        sender.setPort(props.getPort());
        sender.setUsername(props.getUsername());
        sender.setPassword(props.getPassword());

        Properties p = new Properties();
        p.put("mail.smtp.auth", true);
        p.put("mail.smtp.starttls.enable", true);
        sender.setJavaMailProperties(p);

        return sender;
    }
}
