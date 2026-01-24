package com.nova.anonymousplanet.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.logging.filter
 * fileName : MdcClearFilter
 * author : Jinhong Min
 * date : 2025-12-08
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-08      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
public class MdcClearFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }finally {
            MDC.clear();
        }
    }
}
