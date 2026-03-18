package com.nova.anonymousplanet.messaging.event;


import com.nova.anonymousplanet.messaging.topic.core.NovaTopic;

import java.time.LocalDateTime;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.event
 * fileName : NovaEvent
 * author : Jinhong Min
 * date : 2026-02-03
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-03      Jinhong Min      최초 생성
 * ==============================================
 */
public record NovaEvent<T>(
        NovaTopic topic,
        T payload,
        LocalDateTime publishedAt
) {
    public static <T> NovaEvent<T> of(NovaTopic topic, T payload) {
        return new NovaEvent<>(topic, payload, LocalDateTime.now());
    }
}
