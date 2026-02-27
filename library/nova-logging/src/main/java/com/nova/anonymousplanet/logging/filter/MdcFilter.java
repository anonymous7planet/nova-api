package com.nova.anonymousplanet.logging.filter;

import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.logging.util.ClientUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * MdcFilter (Servlet)
 *
 * - Gateway 또는 클라이언트에서 전달한 X-* 헤더들을 MDC에 저장하여
 *   로그에서 자동 표출되도록 한다.
 * - 요청이 종료되면 MDC를 반드시 clear 한다.
 *
 * 사용: Spring Boot 프로젝트에 nova-logging 의존성을 추가하면
 * LoggingAutoConfiguration이 자동으로 등록해준다.
 */
@Slf4j
public class MdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        for (LogContextCode code : LogContextCode.values()) {
            if (!code.isMdcSupport()) continue;

            String mdcKey = code.getMdcKey();

            // 1. Request ID는 추적의 핵심이므로 무조건 새로 생성되기 때문에 갱신)
            if (code == LogContextCode.REQUEST_ID) {
                String requestId = (String) request.getAttribute(LogContextCode.REQUEST_ID.getMdcKey());
                if (requestId != null) MDC.put(mdcKey, requestId);
                continue;
            }

            // 2. 핵심 요구사항: MDC에 이미 값이 없을 때만 Put
            if (MDC.get(mdcKey) == null || MDC.get(mdcKey).isBlank()) {
                String value = resolveValue(request, code);
                if (value != null && !value.isBlank()) {
                    MDC.put(mdcKey, value);
                }
            }
        }

        log.debug("[MdcFilter] MDC populated: traceId={}, requestId={}, userId={}",
                MDC.get(LogContextCode.TRACE_ID.getMdcKey()),
                MDC.get(LogContextCode.REQUEST_ID.getMdcKey()),
                MDC.get(LogContextCode.USER_ID.getMdcKey()));

        filterChain.doFilter(request, response);
    }

    private String resolveValue(HttpServletRequest request, LogContextCode code) {
        // 1. Attribute 우선 (TraceAndRequestIdFilter에서 넘겨준 값)
        Object attr = request.getAttribute(code.getMdcKey());
        if (attr != null) return attr.toString();

        // 1. IP 특수 처리
        if (code == LogContextCode.CLIENT_IP) {
            return ClientUtils.getClientIp(request);
        }

        // 3. 나머지 헤더
        return request.getHeader(code.getHeaderKey());
    }
}
