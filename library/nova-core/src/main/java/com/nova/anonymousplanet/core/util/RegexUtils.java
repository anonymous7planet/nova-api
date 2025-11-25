package com.nova.anonymousplanet.core.util;

import java.util.regex.Pattern;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : RegexUtils
 * author : Jinhong Min
 * date : 2025-11-25
 * description : 
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-25      Jinhong Min      최초 생성
 * ==============================================
 */
public final class RegexUtils {
    private RegexUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /** 이메일 정규식 */
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /** 휴대폰 번호 010-xxxx-xxxx / 010xxxxxxxx */
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^01[0-9]{8,9}$");

    /** 닉네임 (한글/영문/숫자, 2~12자) */
    private static final Pattern NICKNAME_PATTERN =
        Pattern.compile("^[가-힣a-zA-Z0-9]{2,12}$");

    /** 관리자 ID (영문+숫자 4~20자) */
    private static final Pattern ADMIN_ID_PATTERN =
        Pattern.compile("^[a-zA-Z0-9]{4,20}$");

    /** 비밀번호 (8~20자, 대문자/소문자/숫자/특수문자 포함) */
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*+=-]).{8,20}$");

    /** 숫자만 */
    private static final Pattern NUMBER_PATTERN =
        Pattern.compile("^[0-9]+$");

    /** 영문자만 */
    private static final Pattern ALPHA_PATTERN =
        Pattern.compile("^[A-Za-z]+$");


    // === Public Methods ===

    public static boolean isEmailFormat(String value) {
        return match(value, EMAIL_PATTERN);
    }

    public static boolean isPhone(String value) {
        return match(value, PHONE_PATTERN);
    }

    public static boolean isNickname(String value) {
        return match(value, NICKNAME_PATTERN);
    }

    public static boolean isAdminId(String value) {
        return match(value, ADMIN_ID_PATTERN);
    }

    public static boolean isValidPassword(String value) {
        return match(value, PASSWORD_PATTERN);
    }

    public static boolean isNumber(String value) {
        return match(value, NUMBER_PATTERN);
    }

    public static boolean isAlphabet(String value) {
        return match(value, ALPHA_PATTERN);
    }

    private static boolean match(String value, Pattern pattern) {
        if (value == null || value.isBlank()) return false;
        return pattern.matcher(value).matches();
    }

}
