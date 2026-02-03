package com.nova.anonymousplanet.messaging.consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.consumer
 * fileName : AbstractEventConsumer
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
public abstract class AbstractEventConsumer<T> {

    // 각 서비스에서 구체적으로 구현할 알맹이
    protected abstract void onProcess(T payload);

    protected void handle(T payload) {
        log.info("[nova-messaging] 수신 처리 시작: {}", payload.getClass().getSimpleName());
        try {
            onProcess(payload);
        } catch (Exception e) {
            log.error("[nova-messaging] 수신 처리 중 예외 발생!", e);
            throw e;
        }
    }
}