package com.nova.anonymousplanet.persistence.common;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.entity.common
  fileName : BaseEntity
  author : Jinhong Min
  date : 2025-05-02
  description :
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-05-02      Jinhong Min      최초 생성
  ==============================================
 */
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends AbstractAuditable {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}
