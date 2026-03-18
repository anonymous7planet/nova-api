package com.nova.anonymousplanet.notification.messaging.consuemr;

import com.nova.anonymousplanet.messaging.consumer.EventProcessHandler;
import com.nova.anonymousplanet.messaging.event.NovaEvent;
import com.nova.anonymousplanet.messaging.schema.user.UserEvent;
import com.nova.anonymousplanet.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.messaging.consuemr
 * fileName : UserConsumer
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
public class UserEventConsumer {
    private final NotificationService notificationService;
    private final EventProcessHandler eventProcessHandler;

    /**
     * [회원가입 완료] 이벤트 리스너
     */
    @KafkaListener(
            topics = "#{T(com.nova.messaging.topic.UserTopic).REGISTERED.getTopic()}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onUserRegistered(NovaEvent<UserEvent.Registered> event) {
        eventProcessHandler.handle(event, () -> {
            UserEvent.Registered payload = event.payload();

            // NotificationService의 기존 메일 발송 로직 호출
            notificationService.sendEmail(
                    null,                            // recipientId (필요 시 DTO에 추가)
                    payload.email(),                 // 수신자 이메일
                    "WELCOME_CONFIRM",               // DB 관리 템플릿 코드
                    Map.of("userUuid", payload.userUuid()), // 템플릿 변수
                    Collections.emptyList(),         // 첨부파일 없음
                    Collections.emptyList()          // 인라인 이미지 없음
            );
        });
    }

    /**
     * [프로필 정보 수정] 이벤트 리스너 (예시)
     * 새로운 이벤트가 추가되어도 파일 하나에서 관리 가능!
     */
    @KafkaListener(
            topics = "#{T(com.nova.messaging.topic.UserTopic).PROFILE_UPDATED.getTopic()}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onUserProfileUpdated(NovaEvent<UserEvent.Updated> event) {
        eventProcessHandler.handle(event, () -> {
            log.info("사용자 프로필 변경 알림 로직 실행 예정: {}", event.payload().userUuid());
            // 여기에 관련 비즈니스 로직 작성...
        });
    }
}
