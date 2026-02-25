package com.nova.anonymousplanet.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : MaskingUtils
 * author : Jinhong Min
 * date : 2026-02-25
 * description :
 * Project Nova: 공통 마스킹 유틸리티
 * 민감 정보(개인정보)를 로그 출력이나 화면 표시용으로 변환합니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-25      Jinhong Min      최초 생성
 * ==============================================
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaskingUtils {

    // 휴대폰 번호 패턴 (가운데 국번 3~4자리 추출용)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$");

    // 이메일 패턴 (ID와 도메인 분리용)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^([^@]+)@([^@]+)$");

    /**
     * 휴대폰 번호 마스킹 (가운데 4자리)
     * 예: 010-1234-5678 -> 010-****-5678
     */
    public static String phone(String phone) {
        if (!StringUtils.hasText(phone)) return "";

        Matcher matcher = PHONE_PATTERN.matcher(phone);
        if (matcher.matches()) {
            String group1 = matcher.group(1); // 010
            String group3 = matcher.group(3); // 5678
            return String.format("%s-****-%s", group1, group3);
        }

        // 패턴이 맞지 않는 경우 안전을 위해 전체 마스킹 처리 시도
        return phone.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-****-$3");
    }

    /**
     * 성명 마스킹 (이름 중간 글자)
     * 예: 홍길동 -> 홍*동, 남궁민수 -> 남**수
     */
    public static String name(String name) {
        if (!StringUtils.hasText(name)) return "";

        int length = name.length();
        if (length <= 1) return name;
        if (length == 2) return name.charAt(0) + "*";

        // 중간 글자 전체 마스킹
        String start = name.substring(0, 1);
        String end = name.substring(length - 1);
        String middle = "*".repeat(length - 2);

        return start + middle + end;
    }

    /**
     * 이메일 마스킹 (ID 앞 2글자 제외 마스킹)
     * 예: gemini@google.com -> ge****@google.com
     */
    public static String email(String email) {
        if (!StringUtils.hasText(email)) return "";

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.matches()) {
            String id = matcher.group(1);
            String domain = matcher.group(2);

            if (id.length() <= 2) {
                return id + "****@" + domain;
            }
            return id.substring(0, 2) + "****@" + domain;
        }
        return email; // 패턴 불일치 시 원본 반환 (혹은 예외 처리)
    }
}