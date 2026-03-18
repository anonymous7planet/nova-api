package com.nova.anonymousplanet.user.messaging.producer;

import com.nova.anonymousplanet.messaging.topic.UserTopic;
import com.nova.anonymousplanet.messaging.event.NovaEvent;
import com.nova.anonymousplanet.messaging.producer.NovaEventPublisher;
import com.nova.anonymousplanet.messaging.schema.user.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.user.event
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
     * 정보 수정 이벤트 발행 표준화
     */
    public void publishUpdate(UserEvent.Updated payload) {
        NovaEvent<UserEvent.Updated> event = NovaEvent.of(UserTopic.PROFILE_UPDATED, payload);
        novaEventPublisher.publish(event.topic().getTopic(), event);
    }
}