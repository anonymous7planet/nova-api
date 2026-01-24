package com.nova.anonymousplanet.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : RequestHeaderCode
 * author : Jinhong Min
 * date : 2026-01-22
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-22      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
@RequiredArgsConstructor
public enum LogContextCode {
    TRACE_ID("X-Trace-Id", "traceId", true),
    REQUEST_ID("X-Request-Id", "requestId", true),
    USER_ID("X-User-Id", "userId", true),
    USER_UUID("X-User-Uuid", "userUuid", true),
    USER_ROLE("X-User-Role", "userRole", true),
    CLIENT_IP("X-Client-IP", "clientIp", true),
    DEVICE_TYPE("X-Device-Type", "deviceType", true),
    LANG("X-Lang", "lang", true),
    LOCALE("X-Locale", null, false), // MDC 키가 정의되지 않은 경우 대응
    USER_AGENT("User-Agent", null, false);

    private final String headerKey;
    private final String mdcKey;
    private final boolean mdcSupport;
}
