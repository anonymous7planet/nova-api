package com.nova.anonymousplanet.auth.messaging.producer;

import com.nova.anonymousplanet.messaging.topic.UserTopic;
import com.nova.anonymousplanet.messaging.event.NovaEvent;
import com.nova.anonymousplanet.messaging.producer.NovaEventPublisher;
import com.nova.anonymousplanet.messaging.schema.user.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.event
 * fileName : UserEventProducer
 * author : Jinhong Min
 * date : 2026-03-18
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-18      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final NovaEventPublisher novaEventPublisher;

    /**
     * 회원가입 이벤트 발행
     */
    public void publishRegistration(UserEvent.Registered payload) {
        NovaEvent<UserEvent.Registered> event = NovaEvent.of(UserTopic.REGISTERED, payload);
        // userUuid를 Key로 사용하여 순서 보장
        novaEventPublisher.publish(event.topic().getTopic(), event);
        log.info("[Producer] 가입 이벤트 전송 완료: {}", payload.userUuid());
    }
}
