package com.nova.anonymousplanet.messaging.entity;

import com.nova.anonymousplanet.messaging.constant.EmailTemplateTypeCode;
import com.nova.anonymousplanet.messaging.constant.SendStatusCode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.entity
 * fileName : EmailTemplateVariableEntity
 * author : Jinhong Min
 * date : 2025-11-24
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-24      Jinhong Min      최초 생성
 * ==============================================
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class EmailTemplateVariableEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EmailTemplateTypeCode.EmailTemplateTypeCodeConverter.class)
    @Column(name = "template_type_code", length = 50, nullable = false)
    private EmailTemplateTypeCode templateTypeCode;

    @Column(name = "var_key", columnDefinition = "VARCHAR(100)", nullable = false)
    private String varKey;

    @Column(name = "var_value", columnDefinition = "TEXT", nullable = false)
    private String varValue;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "modified_by")
    private Long modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

}
