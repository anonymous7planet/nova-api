package com.nova.anonymousplanet.messaging.schema.user;

import java.time.LocalDateTime;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.schema.user
 * fileName : UserEvent
 * author : Jinhong Min
 * date : 2026-03-18
 * description :
 * [Project Nova] 사용자 관련 이벤트 통합 관리 인터페이스
 * 관련된 모든 사용자 이벤트를 내부 record로 정의하여 응집도를 높입니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-18      Jinhong Min      최초 생성
 * ==============================================
 */
public interface UserEvent {
    /**
     * 회원 가입 완료 이벤트
     */
    record Registered(
            String userUuid,
            String email,
            LocalDateTime registeredAt
    ) implements UserEvent {}

    /**
     * 회원 정보 수정 이벤트
     */
    record Updated(
            String userUuid,
            String nickname,
            String profileImageUrl,
            LocalDateTime updatedAt
    ) implements UserEvent {}
}
