package com.nova.anonymousplanet.common.entity;

import lombok.Getter;

import java.time.LocalDateTime;



@Getter
public abstract class BaseEntity {
    protected Long creator;
    protected Long updater;

    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
