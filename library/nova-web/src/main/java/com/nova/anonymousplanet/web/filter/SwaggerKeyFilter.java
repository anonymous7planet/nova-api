package com.nova.anonymousplanet.web.filter;

import com.nova.anonymousplanet.core.constant.SecurityConstants;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.util.NovaResponseUtils;
import com.nova.anonymousplanet.core.util.PathUtils;
import com.nova.anonymousplanet.web.configuration.properties.SwaggerProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

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

        // 1. Swagger 기능이 꺼져있거나, 검사 대상 경로가 아니면 통과
        if (!properties.enabled() || !isSwaggerPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 보안 검증이 필요한 대상인지 확인 (API 명세 요청 등)
        if (isValidationTarget(request, path)) {
            String requestKey = request.getHeader(properties.headerName());
            if (requestKey == null || !requestKey.equals(properties.headerValue())) {
                NovaResponseUtils.sendError(response, CommonErrorCode.AUTH_FORBIDDEN, "swagger에 접근 권한이 없습니다.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * SecurityConstants에 정의된 전사 공통 Swagger 경로인지 확인
     */
    private boolean isSwaggerPath(String path) {
        // 1. 브라우저가 직접 호출하는 HTML/CSS/JS 리소스는 검증에서 제외 (UI 렌더링 허용)
        return SecurityConstants.SWAGGER_PATHS.stream().anyMatch(pattern -> PathUtils.match(pattern, path));
    }

    /**
     * 실제 보안 키 검증이 필요한 핵심 경로인지 확인
     */
    private boolean isValidationTarget(HttpServletRequest request, String path) {
        // API 명세(JSON) 요청이거나, Referer가 Swagger UI인 경우 검증 대상
        return path.contains("/v3/api-docs") ||
                (request.getHeader("Referer") != null && request.getHeader("Referer").contains("swagger-ui"));
    }
}
