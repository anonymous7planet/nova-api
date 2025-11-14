package com.nova.anonymousplanet.auth.entity;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.domain.user
  fileName : UserEntity
  author : Jinhong Min
  date : 2025-10-27
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-27      Jinhong Min      최초 생성
  ==============================================
 */

import com.nova.anonymousplanet.core.constant.BloodTypeCode;
import com.nova.anonymousplanet.core.constant.GenderCode;
import com.nova.anonymousplanet.core.constant.MbtiCode;
import com.nova.anonymousplanet.core.constant.RoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;
import com.nova.anonymousplanet.core.constant.YesNoCode;
import com.nova.anonymousplanet.core.util.UuidUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 PK

    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String uuid; // 외부 노출용 UUID

    @Comment("이메일(로그인 ID)")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Comment("비밀번호(BCrypt 암호화)")
    @Column(nullable = false, length = 300)
    private String password;


    @Comment("이름")
    @Column(nullable = false, length = 50)
    private String name;

    @Comment("닉네임")
    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Comment("성별")
    @Convert(converter = GenderCode.GenderCodeConverter.class)
    @Column(length = 1)
    private GenderCode gender;

    @Comment("MBTI")
    @Convert(converter = MbtiCode.MbtiCodeConverter.class)
    @Column(length = 4)
    private MbtiCode mbti;

    @Comment("혈액형 (Rh+ / Rh- 포함)")
    @Convert(converter = BloodTypeCode.BloodTypeCodeConverter.class)
    @Column(length = 4)
    private BloodTypeCode bloodType;

    @Comment("회원 상태")
    @Convert(converter = UserStatusCode.UserStatusCodeConverter.class)
    @Column(nullable = false, length = 30)
    private UserStatusCode status;

    @Comment("권한(Role)")
    @Convert(converter = RoleCode.RoleCodeConverter.class)
    @Column(nullable = false, length = 20)
    private RoleCode role;

    @Comment("계정 활성화 여부")
    @Convert(converter = YesNoCode.YesNoCodeConverter.class)
    @Column(nullable = false, length = 1)
    private YesNoCode isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * 사용자 생성 메서드
     * 모든 주요 필드를 지정해 객체를 생성합니다.
     */
    public static UserEntity create(
        String email,
        String encodedPassword,
        String nickname,
        GenderCode gender,
        MbtiCode mbti,
        BloodTypeCode bloodType,
        RoleCode role,
        UserStatusCode status,
        YesNoCode isActive
    ) {
        return UserEntity.builder()
            .uuid(UuidUtils.generate())
            .email(email)
            .password(encodedPassword)
            .nickname(nickname)
            .gender(gender)
            .mbti(mbti)
            .bloodType(bloodType)
            .role(role)
            .status(status)
            .isActive(isActive)
            .build();
    }

}

