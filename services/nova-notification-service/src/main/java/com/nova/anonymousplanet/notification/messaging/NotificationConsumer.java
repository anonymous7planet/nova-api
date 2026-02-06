package com.nova.anonymousplanet.notification.messaging;

import com.nova.anonymousplanet.core.event.email.EmailSendEvent;
import com.nova.anonymousplanet.core.event.NovaEvent;
import com.nova.anonymousplanet.messaging.consumer.AbstractEventConsumer;
import com.nova.anonymousplanet.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.messaging
 * fileName : NotificationConsumer
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
public class NotificationConsumer extends AbstractEventConsumer<NovaEvent<EmailSendEvent>> {

    private final NotificationService notificationService;

    /**
     * Kafka 리스너 진입점
     */
    @KafkaListener(
            topics = "email-send-topic",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeEmailSend(NovaEvent<EmailSendEvent> event) {
        // 부모의 handle을 호출하여 공통 로깅 및 예외 처리를 수행
        super.handle(event);
    }

    /**
     * 추상 클래스의 실제 구현체 (알맹이)
     */
    @Override
    protected void onProcess(NovaEvent<EmailSendEvent> event) {
        // 래퍼에서 알맹이(payload)를 꺼내서 서비스로 전달
        EmailSendEvent payload = event.payload();

        log.info("[NotificationConsumer] Processing email for: {}", payload.email());

        notificationService.sendEmail(
                payload.recipientId(),
                payload.email(),
                payload.templateCode(),
                payload.variables(),
                payload.attachments(),
                payload.inlineImages()
        );
    }
}