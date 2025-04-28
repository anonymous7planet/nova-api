package com.nova.anonymousplanet.auth.domain.common;

import com.nova.anonymousplanet.common.model.AbstractAuditable;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends AbstractAuditable {

    @Override
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    @LastModifiedDate
    @Column(name = "updated_at")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    @LastModifiedBy
    @Column(name = "updated_by")
    public String getUpdatedBy() {
        return updatedBy;
    }
}
