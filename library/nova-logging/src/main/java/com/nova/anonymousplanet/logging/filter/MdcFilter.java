package com.nova.anonymousplanet.logging.filter;

import com.nova.anonymousplanet.logging.util.LoggingConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

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
public class MdcFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;

        // 2) User context
        putIfPresent(LoggingConstants.MDC_USER_ID, req.getHeader(LoggingConstants.HEADER_USER_ID));
        putIfPresent(LoggingConstants.MDC_USER_UUID, req.getHeader(LoggingConstants.HEADER_USER_UUID));
        putIfPresent(LoggingConstants.MDC_USER_ROLE, req.getHeader(LoggingConstants.HEADER_USER_ROLE));

        // 3) client/device
        putIfPresent(LoggingConstants.MDC_CLIENT_IP, headerOrFallback(req, LoggingConstants.HEADER_CLIENT_IP));
        putIfPresent(LoggingConstants.MDC_DEVICE, req.getHeader(LoggingConstants.HEADER_DEVICE));

        // 4) lang/locale
        putIfPresent(LoggingConstants.MDC_LANG, req.getHeader(LoggingConstants.HEADER_LANG));

        log.debug("[MdcFilter] MDC populated: trace={}, request={}, userId={}",
                MDC.get(LoggingConstants.MDC_TRACE),
                MDC.get(LoggingConstants.MDC_REQUEST),
                MDC.get(LoggingConstants.MDC_USER_ID));

        chain.doFilter(request, response);
    }

    private void putIfPresent(String mdcKey, String value) {
        if (value != null && !value.isBlank()) {
            MDC.put(mdcKey, value);
        }
    }

    private String headerOrFallback(HttpServletRequest req, String headerName) {
        String v = req.getHeader(headerName);
        if (v != null && !v.isBlank()) return v;
        // fallback to remoteAddr for client ip
        if (Objects.equals(headerName, LoggingConstants.HEADER_CLIENT_IP)) {
            String remote = req.getRemoteAddr();
            return remote != null ? remote : "unknown";
        }
        return null;
    }
}
