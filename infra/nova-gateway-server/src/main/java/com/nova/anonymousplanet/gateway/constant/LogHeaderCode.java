package com.nova.anonymousplanet.gateway.constant;

import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.constant
 * fileName : LogHeaderCode
 * author : Jinhong Min
 * date : 2025-12-08
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-08      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
public enum LogHeaderCode {

    TRACE_ID("X-Trace-Id"),
    REQUEST_ID("X-Request-Id"),
    USER_ID("X-User-Id"),
    USER_UUID("X-User-Uuid"),
    USER_ROLE("X-User-Role"),
    CLIENT_IP("X-Client-IP"),
    CLIENT_USER_AGENT("X-User-Agent"),
    DEVICE_TYPE("X-Device-Type"),
    OS_TYPE("X-OS-Type"),
    OS_VERSION("X-OS-Version"),
    LANG("X-Lang"),
    LOCALE("X-Locale"),
    APP_VERSION("X-App-Version"),

    USER_AGENT("User-Agent"),
    ACCEPT_LANGUAGE("Accept-Language")
    ;

    private final String key;

    LogHeaderCode(final String key) {
        this.key = key;
    }


    public String getName() {
        return this.name();
    }
}
