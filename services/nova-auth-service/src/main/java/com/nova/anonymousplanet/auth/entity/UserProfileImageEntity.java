package com.nova.anonymousplanet.auth.entity;

import com.nova.anonymousplanet.core.constant.YesNoCode;
import com.nova.anonymousplanet.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

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

    @Comment("이미지 URL")
    @Column(nullable = false)
    private String imageUrl;

    @Comment("대표 이미지 여부")
    @Column(nullable = false, length = 1)
    private YesNoCode isMain;

    @Comment("노출 순서")
    @Column(nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
