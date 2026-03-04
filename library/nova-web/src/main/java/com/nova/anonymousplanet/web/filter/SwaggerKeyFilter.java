package com.nova.anonymousplanet.web.filter;

import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.util.NovaResponseUtils;
import com.nova.anonymousplanet.web.properties.SwaggerProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.web.filter
 * fileName : SwaggerKeyFilter
 * author : Jinhong Min
 * date : 2026-03-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-04      Jinhong Min      최초 생성
 * ==============================================
 */
@Component
@RequiredArgsConstructor
public class SwaggerKeyFilter extends OncePerRequestFilter {

    private final SwaggerProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!properties.enabled() || !isSwaggerPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isValidationTarget(request, path)) {
            String requestKey = request.getHeader(properties.headerName());
            if (requestKey == null || !requestKey.equals(properties.headerValue())) {
                NovaResponseUtils.sendError(response, CommonErrorCode.AUTH_FORBIDDEN, "swagger에 접근 권한이 없습니다.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isSwaggerPath(String path) {
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

    private boolean isValidationTarget(HttpServletRequest request, String path) {
        return path.contains("v3/api-docs") ||
                (request.getHeader("Referer") != null && request.getHeader("Referer").contains("swagger-ui"));
    }
}
