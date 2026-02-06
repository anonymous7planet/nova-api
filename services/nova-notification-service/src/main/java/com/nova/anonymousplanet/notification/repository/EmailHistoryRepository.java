package com.nova.anonymousplanet.notification.repository;

import com.nova.anonymousplanet.notification.domain.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.repository
 * fileName : EmailHistoryRepository
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, Long> {
    // 외부 노출용 UUID로 이력 조회
    Optional<EmailHistoryEntity> findByExternalId(String externalId);
}