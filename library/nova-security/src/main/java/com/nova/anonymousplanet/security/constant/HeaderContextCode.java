package com.nova.anonymousplanet.security.constant;

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
public enum HeaderContextCode {
    TRACE_ID("X-Trace-Id", "traceId", true),
    REQUEST_ID("X-Request-Id", "requestId", false),

    USER_ID("X-User-Id", "userId", true),
    USER_UUID("X-User-Uuid", "userUuid", true),
    USER_ROLE("X-User-Role", "userRole", true),

    CLIENT_IP("X-Client-Ip", "clientIp", true),
    USER_AGENT("User-Agent", "userAgent", true),

    DEVICE_TYPE("X-Device-Type", "deviceType", false),
    OS_TYPE("X-Os-Type", "osType", false),
    OS_VERSION("X-Os-Version", "osVersion", false),
    LANG("X-Lang", "lang", false),
    LOCALE("X-Locale", null, false), // MDC 키가 정의되지 않은 경우 대응
    APP_VERSION("X-App-Version", "appVersion", false),
    ACCEPT_LANGUAGE("Accept-Language", null, false),

    GATEWAY_SECRET("X-Gateway-Secret", null, false),
    SERVICE_NAME("X-Service-Name", null, false)

    ;

    private final String headerKey;
    private final String mdcKey;
    private final boolean mdcSupport;
}
