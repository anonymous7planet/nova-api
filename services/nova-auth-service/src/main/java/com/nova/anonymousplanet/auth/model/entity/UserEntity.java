package com.nova.anonymousplanet.auth.model.entity;

import com.nova.anonymousplanet.common.constant.BloodTypeCode;
import com.nova.anonymousplanet.common.constant.GenderCode;
import com.nova.anonymousplanet.common.constant.MbtiCode;
import com.nova.anonymousplanet.common.constant.UserStatusCode;
import com.nova.anonymousplanet.persistence.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.domain.user
 * fileName : UserEntity
 * author : Jinhong Min
 * date : 2025-04-28
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-28      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, length = 36)
    private String uuid;

    @Column(name = "email", nullable = false, length = 40)
    private String email;

    @Column(name = "password", nullable = false, length = 25)
    private String password;

    @Column(name = "name", nullable = false, length = 20)
    private String name; // 실명 사용

    @Column(name = "nick_name", nullable = false, length = 40)
    private String nickname;

    @Convert(converter = UserStatusCode.UserStatusCodeConverter.class)
    @Column(name ="status", nullable = false, length = 10)
    private UserStatusCode status;

    @Convert(converter = GenderCode.GenderCodeConverter.class)
    @Column(name = "gender", nullable = false, length = 1)
    private GenderCode gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Convert(converter = BloodTypeCode.BloodTypeCodeConverter.class)
    @Column(name = "blood_type", length = 22)
    private BloodTypeCode bloodType;

    @Convert(converter = MbtiCode.MbtiCodeConverter.class)
    @Column(name = "mbti", length = 4)
    private MbtiCode mbti;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl; // 대표 프로필 이미지

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProfileImageEntity> profileImages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserIntroductionEntity> userIntroduction;
}
