package com.nova.anonymousplanet.messaging.repository;

import com.nova.anonymousplanet.messaging.entity.EmailLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.repository
 * fileName : EmailSendLogRepository
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */
public interface EmailLogRepository extends JpaRepository<EmailLogEntity, Long> {

}
