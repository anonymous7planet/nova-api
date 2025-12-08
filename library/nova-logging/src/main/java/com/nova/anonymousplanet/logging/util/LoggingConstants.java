package com.nova.anonymousplanet.logging.util;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.logging.util
 * fileName : LoggingConstants
 * author : Jinhong Min
 * date : 2025-12-08
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-08      Jinhong Min      최초 생성
 * ==============================================
 */
public class LoggingConstants {
    private LoggingConstants() {}

    // 헤더 키
    public static final String HEADER_TRACE_ID = "X-Trace-Id";
    public static final String HEADER_REQUEST_ID = "X-Request-Id";
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USER_UUID = "X-User-Uuid";
    public static final String HEADER_USER_ROLE = "X-User-Role";
    public static final String HEADER_CLIENT_IP = "X-Client-IP";
    public static final String HEADER_DEVICE = "X-Device-Type";
    public static final String HEADER_LANG = "X-Lang";
    public static final String HEADER_LOCALE = "X-Locale";
    public static final String HEADER_USER_AGENT = "User-Agent";

    // MDC 키 (로그 패턴에서 사용)
    public static final String MDC_TRACE = "traceId";
    public static final String MDC_REQUEST = "requestId";
    public static final String MDC_USER_ID = "userId";
    public static final String MDC_USER_UUID = "userUuid";
    public static final String MDC_USER_ROLE = "userRole";
    public static final String MDC_CLIENT_IP = "clientIp";
    public static final String MDC_DEVICE = "deviceType";
    public static final String MDC_LANG = "lang";
}
