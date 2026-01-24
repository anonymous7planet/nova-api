package com.nova.anonymousplanet.core.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : ClientUtils
 * author : Jinhong Min
 * date : 2025-12-08
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-08      Jinhong Min      최초 생성
 * ==============================================
 */
public class ClientUtils {
    private ClientUtils() {}

    public static String getCurrentRequestUri() {
        try {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest();
            return request.getRequestURI();
        } catch (Exception e) {
            return null;
        }
    }

    // IP를 추출할 헤더 목록 (우선순위 순서)
    private static final List<String> IP_HEADER_CANDIDATES = Arrays.asList(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP",
            "X-RealIP",
            "REMOTE_ADDR"
    );

    /**
     * HttpServletRequest에서 클라이언트의 실제 IP 주소를 추출합니다.
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);

            if (ipList != null && !ipList.isBlank() && !"unknown".equalsIgnoreCase(ipList)) {
                // X-Forwarded-For의 경우 여러 IP가 콤마로 연결될 수 있으므로 첫 번째 IP를 가져옴
                return ipList.split(",")[0].trim();
            }
        }

        // 모든 헤더에 정보가 없는 경우 기본 메서드 호출
        return request.getRemoteAddr();
    }
}
