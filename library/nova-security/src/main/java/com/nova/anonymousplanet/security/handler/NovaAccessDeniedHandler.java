package com.nova.anonymousplanet.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.handler
 * fileName : AccessDeniedHandler
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * 403 Forbidden 처리 핸들러
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
public class NovaAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 요청하신 ErrorSet 구조를 Map으로 직접 구성
        Map<String, Object> errorSet = new LinkedHashMap<>();
        errorSet.put("code", "SEC-403");
        errorSet.put("titleMessage", "접근 거부");
        errorSet.put("detailMessage", "해당 요청에 대한 접근 권한이 없습니다.");
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