package com.nova.anonymousplanet.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.model.response.NovaErrorResponse;
import com.nova.anonymousplanet.core.model.response.NovaResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

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

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 날짜 배열화 방지;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(CommonErrorCode.UNAUTHORIZED.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                NovaResponse.fail(NovaErrorResponse.of(CommonErrorCode.UNAUTHORIZED))
        ));
    }
}
