package com.nova.anonymousplanet.auth.entity;

import com.nova.anonymousplanet.auth.dto.v1.command.UserAuthCommand;
import com.nova.anonymousplanet.core.constant.*;
import com.nova.anonymousplanet.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Comment("성별")
    @Column(length = 1)
    private GenderCode gender;

    @Comment("MBTI")
    @Column(length = 4)
    private MbtiCode mbti;

    @Comment("혈액형 (Rh+ / Rh- 포함)")
    @Column(length = 3)
    private BloodTypeCode bloodType;

    @Comment("키(cm)")
    @Column(nullable = false, precision = 5, scale = 2)
    // precision 5, scale 2는 총 5자리 중 소수점 2자리까지 허용 (예: 182.34)
    private BigDecimal height;

    @Comment("생년월일")
    private LocalDate birthDate;

    // --- 학력 상세 (개선) ---
    @Comment("최종 학력 수준")
    private EducationLevelCode educationLevel; // 고졸, 대졸 등

    @Comment("출신 초등학교")
    private String elementarySchool;

    @Comment("출신 중학교")
    private String middleSchool;

    @Comment("출신 고등학교")
    private String highSchool;

    @Comment("출신 대학교")
    private String university;

    @Comment("전공")
    private String major;



    // --- 직업 및 종교 ---
    @Comment("직업군")
    private JobCategoryCode jobCategory;

    @Comment("상세 직업/직장")
    private String jobDetail;

    @Comment("종교")
    private ReligionCode religion;

    @Comment("흡연 여부")
    @Convert(converter = YesNoCode.YesNoCodeConverter.class)
    private YesNoCode isSmoker;

    @Comment("일일 흡연량 (개비)")
    private Integer smokingAmount; // 안 피우면 0

    @Comment("담배 종류")
    private String smokeType;   // 연초, 전자담배 등

    @Comment("음주 여부")
    @Convert(converter = YesNoCode.YesNoCodeConverter.class)
    private YesNoCode isDrinker;

    @Comment("주량 (병/회)")
    private Double drinkingAmount; // 0.5병, 1.5병 등 소수점 허용을 위해 Double

    @Comment("선호 주종")
    private String drinkingType; // 소주, 맥주, 와인 등


    /**
     * 정적 팩토리 메서드 (Command 객체를 받아 생성)
     */
    public static UserProfileEntity create(UserEntity user, UserAuthCommand.UserProfileCommand command) {
        UserProfileEntity profile = new UserProfileEntity();
        profile.user = user;
        profile.gender = command.gender();
        profile.birthDate = command.birthDate();
        profile.height = command.height();
        profile.mbti = command.mbti();
        profile.bloodType = command.bloodType();

        // 학력 정보 매핑
        profile.educationLevel = command.educationLevel();
        profile.elementarySchool = command.elementarySchool();
        profile.middleSchool = command.middleSchool();
        profile.highSchool = command.highSchool();
        profile.university = command.university();
        profile.major = command.major();

        // 직업 및 종교
        profile.jobCategory = command.jobCategory();
        profile.jobDetail = command.jobDetail();
        profile.religion = command.religion();

        // 음주/흡연
        profile.isSmoker = command.isSmoker();
        profile.smokingAmount = command.smokingAmount() != null ? command.smokingAmount() : 0;
        profile.smokeType = command.smokeType();
        profile.isDrinker = command.isDrinker();
        profile.drinkingAmount = command.drinkingAmount() != null ? command.drinkingAmount() : 0.0;
        profile.drinkingType = command.drinkingType();

        return profile;
    }
}
