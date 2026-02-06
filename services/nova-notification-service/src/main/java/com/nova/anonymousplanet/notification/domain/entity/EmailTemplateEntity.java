package com.nova.anonymousplanet.notification.domain.entity;

import com.nova.anonymousplanet.core.constant.YesNoCode;
import com.nova.anonymousplanet.persistence.common.BaseEntity;
import com.nova.anonymousplanet.persistence.converter.JsonListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.domain.entity
 * fileName : NotificationTemplate
 * author : Jinhong Min
 * date : 2026-02-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-04      Jinhong Min      최초 생성
 * ==============================================
 */
@Entity
@Table(name = "tb_email_templates", indexes = {
        @Index(name = "idx_template_code", columnList = "template_code")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailTemplateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_template_id", nullable = false, updatable = false, columnDefinition = "BIGINT COMMENT '이메일 템플릿 고유 번호'")
    private Long id;

    /** 이메일 템플릿 식별 코드 (예: JOIN_WELCOME) */
    @Column(name = "template_code", nullable = false, unique = true, length = 50, columnDefinition = "VARCHAR(50) COMMENT '템플릿 식별 코드'")
    private String templateCode;

    /** 이메일 제목 템플릿 (치환 변수 포함 가능) */
    @Column(name = "subject_part", nullable = false, length = 200, columnDefinition = "VARCHAR(200) COMMENT '이메일 제목 템플릿'")
    private String subjectPart;

    /** 물리적 HTML 템플릿 파일명 (Thymeleaf 파일 경로) */
    @Column(name = "template_file_name", nullable = false, length = 100, columnDefinition = "VARCHAR(100) COMMENT 'Thymeleaf 파일명'")
    private String templateFileName;

    /** 활성화 여부 (1: 활성, 0: 비활성) */
    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1 COMMENT '사용 여부'")
    private YesNoCode isActive;

    /** 템플릿 렌더링에 필요한 필수 변수 목록 (JSON Array) */
    @Convert(converter = JsonListConverter.class)
    @Column(name = "required_variables", columnDefinition = "TEXT COMMENT '필수 변수 목록(JSON)'")
    private List<String> requiredVariables = new ArrayList<>(); // JSON 형태의 필수 파라미터 정의(["userName", "serviceName", "joinDate"])


    // --- 정적 팩토리 메서드 (생성) ---
    public static EmailTemplateEntity create(String templateCode, String subjectPart, String templateFileName, List<String> requiredVariables) {
        EmailTemplateEntity entity = new EmailTemplateEntity();
        entity.templateCode = templateCode;
        entity.subjectPart = subjectPart;
        entity.templateFileName = templateFileName;
        entity.requiredVariables = (requiredVariables != null) ? requiredVariables : new ArrayList<>();
        entity.isActive = YesNoCode.YES;
        return entity;
    }

    // --- 검증 비즈니스 로직 (매우 깔끔해짐) 메일 발송 전에 검증 ---
    public void validateVariables(Map<String, Object> providedVariables) {
        if (this.requiredVariables == null || this.requiredVariables.isEmpty()) {
            return;
        }

        List<String> missingVariables = this.requiredVariables.stream()
                .filter(var -> !providedVariables.containsKey(var) || providedVariables.get(var) == null)
                .toList();

        if (!missingVariables.isEmpty()) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다: " + missingVariables);
        }
    }

    public void activate() { this.isActive = YesNoCode.YES; }
    public void deActivate() { this.isActive = YesNoCode.NO; }
}
