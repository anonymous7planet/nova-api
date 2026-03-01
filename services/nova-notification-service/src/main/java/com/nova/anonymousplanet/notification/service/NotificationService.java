package com.nova.anonymousplanet.notification.service;

import com.nova.anonymousplanet.core.constant.NotificationTypeCode;
import com.nova.anonymousplanet.messaging.event.email.EmailAttachment;
import com.nova.anonymousplanet.messaging.event.email.InlineImage;
import com.nova.anonymousplanet.notification.domain.entity.EmailHistoryEntity;
import com.nova.anonymousplanet.notification.domain.entity.EmailTemplateEntity;
import com.nova.anonymousplanet.notification.dto.v1.SenderDto;
import com.nova.anonymousplanet.notification.model.EmailPayload;
import com.nova.anonymousplanet.notification.provider.NotificationProvider;
import com.nova.anonymousplanet.notification.provider.NotificationProviderFactory;
import com.nova.anonymousplanet.notification.repository.EmailHistoryRepository;
import com.nova.anonymousplanet.notification.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.service.sender
 * fileName : NotificationService
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationProviderFactory providerFactory;
    private final EmailHistoryRepository emailHistoryRepository;
    private final EmailTemplateRepository emailTemplateRepository;

    @Transactional
    public void sendEmail(Long recipientId, String email, String templateCode, Map<String, Object> variables, List<EmailAttachment> attachments, List<InlineImage> inlineImages) {
        // 1. 템플릿 조회 (Project Nova: 고정 데이터는 DB 관리)
        EmailTemplateEntity template = emailTemplateRepository.findByTemplateCode(templateCode)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateCode));

        // 2. 이력 생성 및 저장 (Long ID 전략 및 내부 UUID 생성 포함)
        // Entity 내부에서 createPending 등의 정적 팩토리 메서드 사용 권장
        EmailHistoryEntity history = EmailHistoryEntity.createPending(
                recipientId,
                email,
                template,
                variables
        );
        emailHistoryRepository.save(history);

        try {
            // 3. 발송용 Payload 구성 (Email 전용 데이터 캡슐화)
            EmailPayload payload = EmailPayload.from(history, attachments, inlineImages);

            // 4. SendDto 조립 (공통 식별자와 페이로드 결합)
            SenderDto<EmailPayload> sendDto = new SenderDto<>(
                    history.getId(),        // 내부 관리용 Long ID
                    history.getExternalId(), // 외부 노출용 String UUID
                    payload
            );

            // 5. 전략 패턴을 통한 발송 실행
            NotificationProvider<EmailPayload> sender = providerFactory.getProvider(NotificationTypeCode.EMAIL);
            sender.send(sendDto);

            // 6. 발송 성공 상태 업데이트
            history.success();

        } catch (IllegalArgumentException e) {
            // 잘못된 인자(이메일 형식 오류 등)는 영구 실패로 처리
            log.error("[NotificationService] Email dispatch failed for history: {}", history.getExternalId(), e);
            history.failed(e.getMessage(), true);
            throw e;
        } catch (Exception e) {
            // 그 외 일시적 오류는 재시도 가능 실패로 처리
            log.error("[NotificationService] Email dispatch failed for history: {}", history.getExternalId(), e);
            history.failed(e.getMessage(), false);
            throw e;
        }
    }
}
