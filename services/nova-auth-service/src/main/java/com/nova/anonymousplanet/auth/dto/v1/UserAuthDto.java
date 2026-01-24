package com.nova.anonymousplanet.auth.dto.v1;

import com.nova.anonymousplanet.auth.dto.v1.command.UserAuthCommand;
import com.nova.anonymousplanet.core.constant.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.dto
  fileName : AuthDto
  author : Jinhong Min
  date : 2025-11-04
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-04      Jinhong Min      최초 생성
  ==============================================
 */
public record UserAuthDto() {

    /**
     * 회원가입 요청 DTO
     */
    public record SignupRequest(


            // privacy
            @NotBlank(message = "이메일은 필수입니다.")
            @Email(message = "이메일 형식이 올바르지 않습니다.")
            String email,

            @NotBlank(message = "비밀번호는 필수입니다.")
            @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*])[A-Za-z\\d!@#\\$%\\^&\\*]{8,25}$",
                    message = "비밀번호는 8~25자 내외이며, 영문 대소문자, 숫자, 특수문자를 각각 포함해야 합니다.")
            String password,

            @NotBlank(message = "비밀번호 확인은 필수입니다.")
            String passwordConfirm,

            @NotBlank(message = "이름은 필수 입력값입니다.")
            String name,

            @NotBlank(message = "전화번호는 필수 입력값입니다.")
            @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
            String phoneNumber,

            @NotBlank(message = "본인인증 CI값은 필수입니다.")
            String ci,

            @NotBlank(message = "거주 시/도는 필수입니다.")
            String city,

            @NotBlank(message = "거주 구/군은 필수입니다.")
            String district,


            // profile
            @NotNull(message = "생년월일은 필수입니다.")
            @Past(message = "생년월일은 과거 날짜여야 합니다.")
            LocalDate birthDate,

            @NotNull(message = "성별은 필수입니다.")
            GenderCode gender,

            @NotNull(message = "MBTI는 필수입니다.")
            MbtiCode mbti,

            @NotNull(message = "혈액형은 필수입니다.")
            BloodTypeCode bloodType,

            @NotNull(message = "키 정보는 필수입니다.")
            @DecimalMin(value = "50.0", message = "키는 50cm 이상이어야 합니다.")
            @DecimalMax(value = "250.0", message = "키는 250cm 이하이어야 합니다.")
            BigDecimal height, // 175.5 형태로 전달받음

            // --- 학력 상세 정보 ---
            @NotNull(message = "최종 학력 수준을 선택해주세요.")
            EducationLevelCode educationLevel,

            @NotBlank(message = "출신 초등학교를 입력해주세요.")
            String elementarySchool,

            @NotBlank(message = "출신 중학교를 입력해주세요.")
            String middleSchool,

            @NotBlank(message = "출신 고등학교를 입력해주세요.")
            String highSchool,

            String university, // 대졸 이상이 아닐 수 있으므로 NotBlank 제외

            String major,

            // --- 직업 및 종교 ---
            @NotNull(message = "직업군을 선택해주세요.")
            JobCategoryCode jobCategory,

            @NotBlank(message = "상세 직업 또는 직장을 입력해주세요.")
            String jobDetail,

            @NotNull(message = "종교를 선택해주세요.")
            ReligionCode religion,

            // --- 흡연 상세 ---
            @NotNull(message = "흡연 여부를 선택해주세요.")
            YesNoCode isSmoker,

            @Min(value = 0, message = "흡연량은 0 이상이어야 합니다.")
            Integer smokingAmount,

            String smokeType,

            // --- 음주 상세 ---
            @NotNull(message = "음주 여부를 선택해주세요.")
            YesNoCode isDrinker,

            @Min(value = 0, message = "주량은 0 이상이어야 합니다.")
            Double drinkingAmount,

            String drinkingType,


            /* --- 약관 동의 관련 (필수 항목은 true 검증) --- */
            @NotNull(message = "이용약관 동의는 필수입니다.")
            YesNoCode isTermsAgreed,

            @NotNull(message = "개인정보 수집 동의는 필수입니다.")
            YesNoCode isPrivacyAgreed,

            @NotNull(message = "위치기반 서비스 이용 동의는 필수입니다.")
            YesNoCode isLocationAgreed,

            @NotNull(message = "마케팅 동의 여부를 선택해주세요.")
            YesNoCode isMarketingAgreed,


            // 기본 정보
            @NotNull(message = "로그인 제공자 정보는 필수입니다.")
            LoginProviderCode loginProvider
    ) {
        /**
         * 비밀번호 일치 여부 확인 편의 메서드
         */
        public boolean isPasswordMatch() {
            return password != null && password.equals(passwordConfirm);
        }
        /**
         * 가공된 보안 데이터를 PrivacyCommand로 변환
         */
        public UserAuthCommand.UserPrivacyCommand toPrivacyCommand(
                String encEmail, String encPhoneNumber, String encCi,
                String encName, String encCity, String encDistrict
        ) {
            return new UserAuthCommand.UserPrivacyCommand(
                    encEmail, encPhoneNumber, encCi, encName, encCity, encDistrict
            );
        }

        /**
         * 단순 정보성 데이터를 ProfileCommand로 변환 (암호화 필요 없음)
         */
        public UserAuthCommand.UserProfileCommand toProfileCommand() {
            return new UserAuthCommand.UserProfileCommand(
                    this.gender,
                    this.birthDate,
                    this.height,
                    this.mbti,
                    this.bloodType,
                    this.educationLevel,
                    this.elementarySchool,
                    this.middleSchool,
                    this.highSchool,
                    this.university,
                    this.major,
                    this.jobCategory,
                    this.jobDetail,
                    this.religion,
                    this.isSmoker,
                    this.smokingAmount != null ? this.smokingAmount : 0,
                    this.smokeType,
                    this.isDrinker,
                    this.drinkingAmount != null ? this.drinkingAmount : 0.0,
                    this.drinkingType
            );
        }
        public List<UserAuthCommand.UserProfileImageCommand> toProfileImagesCommand() {
            List<UserAuthCommand.UserProfileImageCommand> profileImages = new ArrayList<>();
            return profileImages;
        }
        public List<UserAuthCommand.UserIntroductionCommand> toIntroductionsCommand() {
            List<UserAuthCommand.UserIntroductionCommand> introductions = new ArrayList<>();
            return introductions;
        }
    }



    /**
     * Login
     * @param email 아이디
     * @param password 비밀번호
     * @param deviceId 기계아이디(프론트에서 생성)
     */
    public record LoginRequest(
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @NotBlank(message = "deviceId는 필수입니다.")
        String deviceId
    ) {
    }

    public record LoginResponse(
        // 1. 사용자 식별 정보
        String email,      // email
        String name,       // 이름
        String nickname,   // 닉네임
        GenderCode gender, // 성별
        // 2. 토큰 정보
        TokenDto.IssueResponse tokenInfo     // Access Token 및 Refresh Token 정보 DTO
    ) {
    }
}
