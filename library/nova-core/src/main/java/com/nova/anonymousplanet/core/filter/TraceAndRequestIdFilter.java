package com.nova.anonymousplanet.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.filter
 * fileName : TraceAndRequestIdFilter
 * author : Jinhong Min
 * date : 2025-12-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-04      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
public class TraceAndRequestIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_HEADER = "X-Trace-Id";  // 요청하나에대한 고유값
    public static final String REQUEST_ID_HEADER = "X-Request-Id"; // 각 서비스요청에대한 고유값
    public static final String MDC_TRACE_KEY = "traceId";
    public static final String MDC_REQUEST_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = headerOrGenerate(request, TRACE_ID_HEADER);
        String requestId = UUID.randomUUID().toString();

        MDC.put(MDC_TRACE_KEY, traceId);
        MDC.put(MDC_REQUEST_KEY, requestId);

        // 응답 헤더에도 넣어 downstream(클라이언트)에서 확인 가능
        response.setHeader(TRACE_ID_HEADER, traceId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        log.debug("[TraceAndRequestIdFilter] Applied traceId={}, requestId={}", traceId, requestId);
        filterChain.doFilter(request, response);
//      MDC.clear(); // clear는 MDCClearFilter에서 한다
    }


    private String headerOrGenerate(HttpServletRequest request, String headerName) {
        String val = request.getHeader(headerName);
        if (val == null || val.isBlank()) {
            val = UUID.randomUUID().toString();
            log.debug("[TraceAndRequestIdFilter] Header {} not found. Generated {}", headerName, val);
        }
        return val;
    }

}
