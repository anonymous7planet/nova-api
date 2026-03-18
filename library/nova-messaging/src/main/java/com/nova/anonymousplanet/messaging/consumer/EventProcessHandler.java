package com.nova.anonymousplanet.messaging.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.consumer
 * fileName : EventProcessHandler
 * author : Jinhong Min
 * date : 2026-03-18
 * description :
 * 파일 하나에 여러개의 컨슈머 (도메인기준)
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-18      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
public class EventProcessHandler {

    /**
     * 공통 로깅 및 예외 처리를 수행하며 비즈니스 로직을 실행합니다.
     * @param event 수신된 이벤트 객체 (로깅용)
     * @param logic 실행할 실제 비즈니스 로직
     */
    public <T> void handle(T event, Runnable logic) {
        String eventName = event.getClass().getSimpleName();

        log.info("[Event-Consumer] >>> Start: {}", eventName);

        try {
            logic.run(); // 실제 서비스 로직 실행
            log.info("[Event-Consumer] <<< Success: {}", eventName);
        } catch (Exception e) {
            log.error("[Event-Consumer] !!! Failed: {} | Reason: {}", eventName, e.getMessage(), e);
            // 필요 시 여기서 공통 에러 알림(Slack 등)이나 DLQ 처리를 수행할 수 있습니다.
            throw e;
        }
    }
}
