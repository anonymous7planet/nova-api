package com.nova.anonymousplanet.logging.filter;

import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.core.util.UUIDUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.filter
 * fileName : TraceAndRequestIdFilter
 * author : Jinhong Min
 * date : 2025-12-04
 * description : 처음 각 서비스 진입시 traceI와 requestId를 필요시 추가해준다
 * traceId는 요청하나에대한 고유값
 * requestId는 각 서비스 요청하나에대한 고유값
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-04      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
public class TraceAndRequestIdFilter extends OncePerRequestFilter {
    private final String serviceName;

    public TraceAndRequestIdFilter(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Trace ID 처리 (헤더에 있으면 사용, 없으면 생성)
        String traceId = headerOrGenerate(request, LogContextCode.TRACE_ID.getHeaderKey());

        // 2. Request ID 처리 (서비스명 Prefix 포함) - 무조건 새로 생성
        String requestId = UUIDUtils.generateWithPrefix(serviceName);

        // 3. 다음 필터(MDCFilter 등)에서 쓸 수 있도록 request에 저장
        request.setAttribute(LogContextCode.TRACE_ID.getMdcKey(), traceId);
        request.setAttribute(LogContextCode.REQUEST_ID.getMdcKey(), requestId);

        // 4. 응답 헤더에도 넣어 downstream(클라이언트)에서 확인 가능
        response.setHeader(LogContextCode.TRACE_ID.getHeaderKey(), traceId);
        response.setHeader(LogContextCode.REQUEST_ID.getHeaderKey(), requestId);

        log.debug("[TraceAndRequestIdFilter] Applied traceId={}, requestId={}", traceId, requestId);

        filterChain.doFilter(request, response);
    }


    private String headerOrGenerate(HttpServletRequest request, String headerName) {
        String val = request.getHeader(headerName);
        if (val == null || val.isBlank()) {
            val = UUIDUtils.generate();
            log.debug("[TraceAndRequestIdFilter] Header {} not found. Generated {}", headerName, val);
        }
        return val;
    }
}
