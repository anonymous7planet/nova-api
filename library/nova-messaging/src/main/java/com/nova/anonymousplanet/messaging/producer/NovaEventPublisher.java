package com.nova.anonymousplanet.messaging.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.producer
 * fileName : NovaEventPublisher
 * author : Jinhong Min
 * date : 2026-02-03
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-03      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NovaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * @param topic   발행할 카프카 토픽
     * @param payload 전송할 데이터 (Record 등)
     */
    public void publish(String topic, Object payload) {
        log.info("[nova-messaging] 전송 시도 -> Topic: {}", topic);

        kafkaTemplate.send(topic, payload)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("[nova-messaging] 전송 성공 | Topic: {} | Offset: {}",
                                topic, result.getRecordMetadata().offset());
                    } else {
                        log.error("[nova-messaging] 전송 실패 | Topic: {} | Error: {}",
                                topic, ex.getMessage());
                    }
                });
    }
}
