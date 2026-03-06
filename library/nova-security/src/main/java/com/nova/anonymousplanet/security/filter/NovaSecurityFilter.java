package com.nova.anonymousplanet.security.filter;

import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.security.constant.UserRoleCode;
import com.nova.anonymousplanet.security.constant.UserStatusCode;
import com.nova.anonymousplanet.security.context.UserContext;
import com.nova.anonymousplanet.security.context.UserInfo;
import com.nova.anonymousplanet.security.handler.NovaAccessDeniedHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.filter
 * fileName : NovaSecurityFilter
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@RequiredArgsConstructor
public class NovaSecurityFilter extends OncePerRequestFilter {

    private final String gatewaySecret;

    private final NovaAccessDeniedHandler accessDeniedHandler; // 추가

    private final String[] serviceWhiteList;
    private final String[] commonWhiteList; // COMMON_WHITE_LIST 주입

    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 1. 서비스 전용 화이트리스트 검사
        boolean isServiceWhiteList = Arrays.stream(serviceWhiteList)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));

        // 2. 공통 화이트리스트 검사
        boolean isCommonWhiteList = Arrays.stream(commonWhiteList)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));

        return isServiceWhiteList || isCommonWhiteList;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String incomingSecret = request.getHeader(LogContextCode.GATEWAY_SECRET.getHeaderKey());


        // 1. Secret 검증은 여전히 필수 (Gateway든 Feign이든 이게 없으면 차단)
        if (incomingSecret == null || !incomingSecret.equals(gatewaySecret)) {
            // [수정] sendError 대신 커스텀 핸들러의 로직을 직접 실행
            accessDeniedHandler.handle(request, response,
                    new AccessDeniedException("Invalid Internal Secret"));
            return;
        }

        // 2. 서비스 간 직접 호출 시에는 'X-User-Id'가 없을 수도 있음 (Batch 작업 등)
        // 이럴 경우를 대비해 Null 체크 후 비어있을 때의 처리를 명확히 함
        String userId = request.getHeader(LogContextCode.USER_ID.getHeaderKey());
        String userUuid = request.getHeader(LogContextCode.USER_UUID.getHeaderKey());
        String userRole = request.getHeader(LogContextCode.USER_ROLE.getHeaderKey());
        String userStatus = request.getHeader(LogContextCode.USER_STATUS.getHeaderKey());
        String ipAddress = request.getRemoteAddr();

        try {
            if (userId != null && !userId.isBlank()) {
                UserContext.setUserInfo(new UserInfo(Long.parseLong(userId), userUuid, UserRoleCode.from(userRole), UserStatusCode.from(userStatus), ipAddress));
            } else {
                // [선택] 서비스 간 시스템 통신일 경우 특정 'SYSTEM' 권한 ID를 부여하거나 비워둠
                log.debug("Internal service call without User context");
            }
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
