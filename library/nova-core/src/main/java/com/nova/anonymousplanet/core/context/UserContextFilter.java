package com.nova.anonymousplanet.core.context;

import com.nova.anonymousplanet.core.constant.RoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.filter
  fileName : UserContextFilter
  author : Jinhong Min
  date : 2025-11-06
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-06      Jinhong Min      최초 생성
  ==============================================
 */

@Component
public class UserContextFilter extends OncePerRequestFilter {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_UUID = "X-User-Uuid";
    private static final String HEADER_USER_ROLE = "X-User-Role";
    private static final String HEADER_USER_STATUS = "X-User-Status";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String userId = request.getHeader(HEADER_USER_ID);
            String userUuid = request.getHeader(HEADER_USER_UUID);
            String role = request.getHeader(HEADER_USER_ROLE);
            String status = request.getHeader(HEADER_USER_STATUS);
            String ipAddress = request.getRemoteAddr();

            if (userId != null && userUuid != null) {
                UserContext userContext = new UserContext(Long.valueOf(userId), userUuid, RoleCode.fromCode(role),
                    UserStatusCode.fromCode(status), ipAddress);
                UserContextHolder.set(userContext);
            }
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }

    }
}