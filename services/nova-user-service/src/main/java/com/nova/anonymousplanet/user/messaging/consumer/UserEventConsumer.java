package com.nova.anonymousplanet.user.messaging.consumer;

import com.nova.anonymousplanet.messaging.consumer.AbstractEventConsumer;
import com.nova.anonymousplanet.messaging.event.NovaEvent;
import com.nova.anonymousplanet.messaging.schema.user.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.user.event.consumer
 * fileName : UserEventConsumer
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
public class UserEventConsumer extends AbstractEventConsumer<NovaEvent<? extends UserEvent>> {

//    private final UserApplicationService userApplicationService;

    // T()를 사용하여 Enum 클래스에 접근하고 메서드를 호출합니다.
    @KafkaListener(topics ="#{T(com.nova.anonymousplanet.messaging.topic.UserTopic).REGISTERED.getTopic()}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeRegistration(NovaEvent<UserEvent.Registered> event) {
        super.handle(event);
    }

    @KafkaListener(topics = "#{T(com.nova.anonymousplanet.messaging.topic.UserTopic).PROFILE_UPDATED.getTopic()}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUpdate(NovaEvent<UserEvent.Updated> event) {
        super.handle(event);
    }

    @Override
    protected void onProcess(NovaEvent<? extends UserEvent> event) {
        // 다형성을 활용하여 타입별 비즈니스 로직 분기
        if (event.payload() instanceof UserEvent.Registered registered) {
//            userApplicationService.createProfile(registered);
        } else if (event.payload() instanceof UserEvent.Updated updated) {
//            userApplicationService.syncProfile(updated);
        }
    }
}
