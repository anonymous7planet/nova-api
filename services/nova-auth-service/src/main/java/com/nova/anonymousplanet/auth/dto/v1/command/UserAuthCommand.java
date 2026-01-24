package com.nova.anonymousplanet.auth.dto.v1.command;

import com.nova.anonymousplanet.core.constant.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.dto.v1.command
 * fileName : AuthCommand
 * author : Jinhong Min
 * date : 2026-01-07
 * description : 
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-07      Jinhong Min      최초 생성
 * ==============================================
 */
public record UserAuthCommand() {
    public record UserSignupCommand(
            UserPrivacyCommand privacy,
            UserProfileCommand profile,
            List<UserProfileImageCommand> profileImages,
            List<UserIntroductionCommand> introductions

    ) {
        public static UserSignupCommand of(
                UserPrivacyCommand privacy, UserProfileCommand profile,
                List<UserProfileImageCommand> profileImages, List<UserIntroductionCommand> introductions
        ) {
            return new UserSignupCommand(privacy, profile, profileImages, introductions);
        }
    }

    public record UserPrivacyCommand(
            String email,      // 암호화
            String phoneNumber,// 암호화
            String ci,         // 암호화
            String name,       // 암호화
            String city,       // 암호화
            String district    // 암호화
    ) {}

    public record UserProfileCommand(
            GenderCode gender,
            LocalDate birthDate,
            BigDecimal height,
            MbtiCode mbti,
            BloodTypeCode bloodType,

            // 학력 관련
            EducationLevelCode educationLevel,
            String elementarySchool,
            String middleSchool,
            String highSchool,
            String university,
            String major,

            // 직업 및 종교
            JobCategoryCode jobCategory,
            String jobDetail,
            ReligionCode religion,

            // 음주/흡연
            YesNoCode isSmoker,
            Integer smokingAmount,
            String smokeType,
            YesNoCode isDrinker,
            Double drinkingAmount,
            String drinkingType
    ) {}

    public record UserProfileImageCommand() {}
    public record UserIntroductionCommand() {}
}
