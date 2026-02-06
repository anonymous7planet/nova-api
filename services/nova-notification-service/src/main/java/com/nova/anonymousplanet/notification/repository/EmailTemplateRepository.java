package com.nova.anonymousplanet.notification.repository;

import com.nova.anonymousplanet.notification.domain.entity.EmailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.repository
 * fileName : EmailTemplateRepository
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity, Long> {
    // 템플릿 코드로 템플릿 정보 조회 (예: "WELCOME_MAIL")
    Optional<EmailTemplateEntity> findByTemplateCode(String templateCode);
}