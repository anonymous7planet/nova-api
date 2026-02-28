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
    USER_AGENT("User-Agent", null, false),

    DEVICE_TYPE("X-Device-Type", "deviceType", true),
    OS_TYPE("X-Os-Type", "osType", false),
    OS_VERSION("X-Os-Version", "osVersion", false),

    LANG("X-Lang", "lang", true),
    LOCALE("X-Locale", null, false), // MDC 키가 정의되지 않은 경우 대응
    APP_VERSION("X-App-Version", "appVersion", false),
    ACCEPT_LANGUAGE("Accept-Language", null, false),

    REQUEST_PATH("X-Request-Path", "path", true),
    ;

    private final String headerKey;
    private final String mdcKey;
    private final boolean mdcSupport;
}
