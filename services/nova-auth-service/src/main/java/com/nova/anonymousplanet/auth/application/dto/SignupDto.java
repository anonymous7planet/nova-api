package com.nova.anonymousplanet.auth.application.dto;

import com.nova.anonymousplanet.common.annotation.PasswordMatch;
import com.nova.anonymousplanet.common.constant.BloodTypeCode;
import com.nova.anonymousplanet.common.constant.GenderCode;
import com.nova.anonymousplanet.common.constant.MbtiCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.application.dto
 * fileName : SignupDto
 * author : Jinhong Min
 * date : 2025-04-30
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-30      Jinhong Min      최초 생성
 * ==============================================
 */
public class SignupDto {

    @PasswordMatch
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SignupRequestDto {

        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*])[A-Za-z\\d!@#\\$%\\^&\\*]{8,25}$",
                message = "비밀번호는 최소 8자 이상 최대 25자이하, 영문 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
        private String password;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*])[A-Za-z\\d!@#\\$%\\^&\\*]{8,25}$",
                message = "비밀번호는 최소 8자 이상 최대 25자이하, 영문 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
        private String passwordConfirm;

        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        private String nickname;

        @NotNull(message = "성별은 필수 입력값입니다.")
        private GenderCode gender;

        @NotNull(message = "생년월일은 필수 입력값입니다.")
        private LocalDate birthDate;

        @NotNull(message = "혈액형은 필수 입력값입니다.")
        private BloodTypeCode bloodType;

        @NotNull(message = "MBTI는 필수 입력값입니다.")
        private MbtiCode mbti;


        /**
        private String introductionTitle;
        private String introductionContent;
        private List<String> profileImageUrls;
         */
    }
}
