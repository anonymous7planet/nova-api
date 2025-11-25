package com.nova.anonymousplanet.auth.entity;

import com.nova.anonymousplanet.core.constant.YesNoCode;
import com.nova.anonymousplanet.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.entity
 * fileName : UserProfileEimageEntity
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
@Table(name = "tb_profile_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserProfileImageEntity extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @Convert(converter = YesNoCode.YesNoCodeConverter.class)
    @Column(nullable = false, length = 1)
    private YesNoCode isMain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
