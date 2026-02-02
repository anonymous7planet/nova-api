package com.nova.anonymousplanet.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.handler
 * fileName : NovaAuthenticationEntryPoint
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * 401 Unauthorized 처리 핸들러
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
public class NovaAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 요청하신 ErrorSet 구조를 Map으로 직접 구성
        Map<String, Object> errorSet = new LinkedHashMap<>();
        errorSet.put("code", "SEC-401");
        errorSet.put("titleMessage", "인증 실패");
        errorSet.put("detailMessage", "로그인 정보가 없거나 유효하지 않습니다.");
        errorSet.put("validationErrors", Collections.emptyList());

        // 최종 응답 형태 구성
        Map<String, Object> rootResponse = new LinkedHashMap<>();
        rootResponse.put("success", false);
        rootResponse.put("data", null);
        rootResponse.put("error", errorSet);
        rootResponse.put("timestamp", LocalDateTime.now().toString());

        response.getWriter().write(objectMapper.writeValueAsString(rootResponse));
    }
}
