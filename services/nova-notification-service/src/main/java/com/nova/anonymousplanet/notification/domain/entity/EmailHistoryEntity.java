package com.nova.anonymousplanet.notification.domain.entity;

import com.nova.anonymousplanet.core.constant.NotificationStatusCode;
import com.nova.anonymousplanet.persistence.common.BaseEntity;
import com.nova.anonymousplanet.persistence.converter.CryptoConverter;
import com.nova.anonymousplanet.persistence.converter.JsonMapConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.domain.entity
 * fileName : EmailHistoryEntity
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */
@Entity
@Table(name = "tb_email_history", indexes = {
        @Index(name = "idx_external_id", columnList = "external_id"),
        @Index(name = "idx_recipient_id", columnList = "recipient_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at") // BaseEntity의 생성일자 기반 조회용
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailHistoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_history_id", nullable = false, updatable = false, columnDefinition = "BIGINT COMMENT '이메일 발송 이력 고유 번호'")
    private Long id;

    /** 외부 노출 및 추적용 UUID (String 타입) */
    @Column(name = "external_id", nullable = false, unique = true, length = 36, columnDefinition = "VARCHAR(36) COMMENT '외부 참조용 UUID'")
    private String externalId;

    /** 수신자 내부 ID (회원일 경우 PK, 비회원은 null) */
    @Column(name = "recipient_id", nullable = true, columnDefinition = "BIGINT COMMENT '수신자 내부 식별 ID (비회원 가능)'")
    private Long recipientId;

    /** 암호화된 수신 이메일 주소 */
    @Convert(converter = CryptoConverter.class)
    @Column(name = "recipient_email", nullable = false, length = 512, columnDefinition = "VARCHAR(512) COMMENT '암호화된 수신 이메일 주소'")
    private String recipientEmail;

    /** 발송에 사용된 템플릿 참조 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_template_id", nullable = false, foreignKey = @ForeignKey(name = "fk_email_history_template"), columnDefinition = "BIGINT COMMENT '참조된 이메일 템플릿 ID'")
    private EmailTemplateEntity template;

    /** 발송 당시 치환된 데이터 세트 (JSON) */
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "variables", columnDefinition = "LONGTEXT COMMENT '발송 데이터 세트(JSON)'")
    private Map<String, Object> variables;

    /** 발송 상태 (PENDING, SUCCESS, FAILED, RETRYING 등) */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "VARCHAR(20) COMMENT '발송 상태'")
    private NotificationStatusCode status;

    /** 현재까지 시도된 재시도 횟수 */
    @Column(name = "retry_count", nullable = false, columnDefinition = "INT DEFAULT 0 COMMENT '현재 재시도 횟수'")
    private int retryCount;

    /** 마지막 실패 사유 */
    @Column(name = "error_message", columnDefinition = "TEXT COMMENT '실패 사유 메시지'")
    private String errorMessage;

    /** 실제 최종 발송 완료 시각 */
    @Column(name = "sent_at", columnDefinition = "DATETIME COMMENT '최종 발송 성공 시각'")
    private LocalDateTime sentAt;

    // --- 정적 팩토리 메서드 ---
    public static EmailHistoryEntity createPending(Long recipientId, String email, EmailTemplateEntity template, Map<String, Object> variables) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.externalId = UUID.randomUUID().toString();
        entity.recipientId = recipientId;
        entity.recipientEmail = email;
        entity.template = template;
        entity.variables = variables;
        entity.status = NotificationStatusCode.PENDING;
        entity.retryCount = 0;
        return entity;
    }

    // --- 비즈니스 로직 (Domain Methods) ---

    /**
     * [비즈니스 로직] 발송 완료 상태로 변경
     */
    public void success() {
        this.status = NotificationStatusCode.SUCCESS;
        this.sentAt = LocalDateTime.now();
        this.errorMessage = null; // 성공 시 기존 에러 메시지 초기화
    }


    /**
     * [비즈니스 로직] 발송 실패 상태로 변경
     */
    public void failed(String errorMessage, boolean isPermanent) {
        this.errorMessage = errorMessage;
        if (isPermanent) {
            this.status = NotificationStatusCode.VALIDATION_FAILED;
        } else {
            this.status = NotificationStatusCode.FAILED;
            this.retryCount++;
        }
    }

    public void markAsRetrying() {
        this.status = NotificationStatusCode.RETRYING;
    }

}
