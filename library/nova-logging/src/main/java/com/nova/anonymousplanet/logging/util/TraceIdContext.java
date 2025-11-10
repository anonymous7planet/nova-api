package com.nova.anonymousplanet.logging.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.logging.util
 * fileName : TraceIdContext
 * author : Jinhong Min
 * date : 2025-11-09
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-09      Jinhong Min      최초 생성
 * ==============================================
 */
public class TraceIdContext {
    private static final String TRACE_ID_KEY = "traceId";

    public static void init() {
        MDC.put(TRACE_ID_KEY, UUID.randomUUID().toString().substring(0, 8));
    }

    public static void clear() {
        MDC.remove(TRACE_ID_KEY);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }
}
