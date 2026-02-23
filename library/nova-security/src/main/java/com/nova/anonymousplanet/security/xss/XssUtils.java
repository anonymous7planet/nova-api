package com.nova.anonymousplanet.security.xss;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.util
 * fileName : XssUtils
 * author : Jinhong Min
 * date : 2026-02-23
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-23      Jinhong Min      최초 생성
 * ==============================================
 */
public class XssUtils {
    // 일반 텍스트용 (모든 태그 -> 글자)
    public static String escape(String value) {
        return value == null ? null : StringEscapeUtils.escapeHtml4(value);
    }

    // 웹 에디터용 (위험 태그만 제거, HTML 유지)
    public static String sanitize(String html) {
        return html == null ? null : Jsoup.clean(html, Safelist.relaxed());
    }
}
