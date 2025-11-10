package com.nova.anonymousplanet.auth.dto;

import com.nova.anonymousplanet.core.constant.BloodTypeCode;
import com.nova.anonymousplanet.core.constant.GenderCode;
import com.nova.anonymousplanet.core.constant.MbtiCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

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
public record AuthDto() {

    /**
     * 회원가입 요청 DTO
     */
    public record SignupRequest(
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*])[A-Za-z\\d!@#\\$%\\^&\\*]{8,25}$",
            message = "비밀번호는 최소 8자 이상 최대 25자이하, 영문 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
        String password,

        @NotBlank(message = "비밀번호 확인은 필수입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*])[A-Za-z\\d!@#\\$%\\^&\\*]{8,25}$",
            message = "비밀번호는 최소 8자 이상 최대 25자이하, 영문 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
        String passwordConfirm,

        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @NotNull(message = "생년월일은 필수 입력값입니다.")
        LocalDate birthDate,

        @NotNull(message = "성별은 필수 입력값입니다.")
        GenderCode gender,

        @NotNull(message = "MBTI는 필수 입력값입니다.")
        MbtiCode mbti,

        @NotNull(message = "혈액형은 필수 입력값입니다.")
        BloodTypeCode bloodType
    ) {}


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
        TokenDto.IssueResponse accessToken,
        TokenDto.IssueResponse refreshToken
    ) {
    }
}
