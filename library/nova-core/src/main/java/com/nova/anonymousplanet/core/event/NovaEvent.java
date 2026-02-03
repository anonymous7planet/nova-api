package com.nova.anonymousplanet.core.event;

import com.nova.anonymousplanet.core.constant.NovaEventType;

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
        NovaEventType type,
        T payload,
        LocalDateTime publishedAt
) {
    public static <T> NovaEvent<T> of(NovaEventType type, T payload) {
        return new NovaEvent<>(type, payload, LocalDateTime.now());
    }
}
