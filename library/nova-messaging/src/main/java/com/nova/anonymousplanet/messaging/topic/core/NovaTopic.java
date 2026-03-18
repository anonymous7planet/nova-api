package com.nova.anonymousplanet.messaging.topic.core;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.core
 * fileName : NovaTopic
 * author : Jinhong Min
 * date : 2026-03-18
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-18      Jinhong Min      최초 생성
 * ==============================================
 */
public interface NovaTopic {

    String getServiceName();
    String getEventName();

    /**
     * 토픽 명명 규칙: nova.{service}.{event}.v1
     */
    default String getTopic() {
        return String.format("nova.%s.%s.v1", getServiceName(), getEventName());
    }
}
