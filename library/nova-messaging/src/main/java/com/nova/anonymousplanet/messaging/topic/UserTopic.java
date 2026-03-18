package com.nova.anonymousplanet.messaging.topic;

import com.nova.anonymousplanet.messaging.topic.core.NovaTopic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.topic.core
 * fileName : UserTopic
 * author : Jinhong Min
 * date : 2026-03-18
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-18      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
@RequiredArgsConstructor
public enum UserTopic implements NovaTopic {

    REGISTERED("user", "registration", "신규 회원가입"),
    PROFILE_UPDATED("user", "profile-update", "회원 프로필 수정"),

    ;

    private final String serviceName;
    private final String eventName;
    private final String description;
}
