package com.nova.anonymousplanet.notification.provider;

import com.nova.anonymousplanet.core.constant.NotificationTypeCode;
import com.nova.anonymousplanet.notification.dto.v1.SenderDto;
import com.nova.anonymousplanet.notification.model.EmailPayload;
import com.nova.anonymousplanet.notification.service.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.provider
 * fileName : EmailNotificationProvider
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
@Component
@RequiredArgsConstructor
public class EmailNotificationProvider implements NotificationProvider<EmailPayload>{

    // 실제 SMTP 전송 기술을 가진 인프라 빈 (이름을 SmtpEmailSender로 변경 권장)
    private final EmailSender emailSender;

    @Override
    public NotificationTypeCode getType() {
        return NotificationTypeCode.EMAIL;
    }

    @Override
    @Retryable(
            retryFor = { Exception.class },
            maxAttemptsExpression = "${nova.notification.email.retry.max-attempts:3}",
            backoff = @Backoff(
                    delayExpression = "${nova.notification.email.retry.initial-delay:2000}",
                    multiplierExpression = "${nova.notification.email.retry.multiplier:2.0}"
            )
    )
    public void send(SenderDto<EmailPayload> sendDto) {
        log.info("[EmailSender] Dispatching history: {} (External: {})",
                sendDto.historyId(), sendDto.externalId());

        // Payload에 이미 모든 정보(첨부파일 포함)가 들어있으므로 바로 전달
        emailSender.send(sendDto.payload());
    }


    /**
     * 모든 재시도가 실패했을 때 실행되는 메서드
     * @param e 발생한 예외
     * @param sendDto 원래 전달받았던 데이터
     */
    @Recover
    public void recover(Exception e, SenderDto<EmailPayload> sendDto) {
        log.error("[EmailSender] FATAL: All retry attempts failed for HistoryId: {}. Reason: {}",
                sendDto.historyId(), e.getMessage());
        // 여기에 추가 로직 작성 가능:
        // 1. 모니터링 시스템(Sentry, Prometheus)에 메트릭 전송
        // 2. 관리자 슬랙 알림 발송 (별도 서비스 호출)
        // 3. 알림 서비스 내의 '최종 실패 처리' 이벤트 발행 (Kafka 등)

        // Note: DB 상태 업데이트는 NotificationService의 catch 블록에서 수행되므로
        // 여기서 직접 DB를 건드리는 것보다 로그 및 외부 알림에 집중하는 것이 좋습니다.
    }

}
