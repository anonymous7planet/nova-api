package com.nova.anonymousplanet.auth.entity;

import com.nova.anonymousplanet.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.entity
 * fileName : UserProfileEntity
 * author : Jinhong Min
 * date : 2025-11-25
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-25      Jinhong Min      최초 생성
 * ==============================================
 */

@Entity
@Table(name = "tb_user_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private Integer age;

    private Integer height;

    @Column(length = 100)
    private String job;

    @Column(length = 100)
    private String education;

    @Column(length = 200)
    private String residence;

}
