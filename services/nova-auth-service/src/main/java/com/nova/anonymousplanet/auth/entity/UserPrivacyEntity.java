package com.nova.anonymousplanet.auth.entity;

import com.nova.anonymousplanet.auth.dto.v1.command.UserAuthCommand;
import com.nova.anonymousplanet.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.entity
 * fileName : UserPrivacyEntity
 * author : Jinhong Min
 * date : 2025-12-26
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-26      Jinhong Min      최초 생성
 * ==============================================
 */
@Entity
@Table(name = "tb_user_privacy")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserPrivacyEntity extends BaseEntity {

    @Id
    private Long userId; // 내부 PK

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // AES-256 암호화되어 저장되는 실제 데이터
    @Comment("이메일")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Comment("전화번호")
    @Column(nullable = false, unique = true, length = 100)
    private String phoneNumber;

    @Comment("전화번호 인증 CI")
    @Column(nullable = false, unique = true, length = 100)
    private String ci;  // 본인 인증시 넘어오는 데이터, 전화번호가 변경되어도 값이 같다

    @Comment("실명")
    @Column(nullable = false, length = 100)
    private String name; // 실명

    @Comment("거주 시/도(암호화)")
    @Column(nullable = false)
    private String city;

    @Comment("거주 구/군(암호화)")
    @Column(nullable = false)
    private String district;

    /**
     * 정적 팩토리 메서드 업데이트
     */
    public static UserPrivacyEntity create(UserEntity user, UserAuthCommand.UserPrivacyCommand command) {
        UserPrivacyEntity privacy = new UserPrivacyEntity();

        privacy.user = user;
        privacy.email = command.email();
        privacy.phoneNumber = command.phoneNumber();
        privacy.ci = command.ci();
        privacy.name = command.name();
        privacy.city = command.city();
        privacy.district = command.district();

        return privacy;
    }
}
