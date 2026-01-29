package com.nova.anonymousplanet.gateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;

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
    public static String getClientIp(ServerHttpRequest request) {
        if (request == null) {
            return "unknown";
        }

        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeaders().getFirst(header);

            if (ipList != null && !ipList.isBlank() && !"unknown".equalsIgnoreCase(ipList)) {
                // X-Forwarded-For의 경우 여러 IP가 콤마로 연결될 수 있으므로 첫 번째 IP를 가져옴
                return ipList.split(",")[0].trim();
            }
        }

        if (request.getRemoteAddress() != null && request.getRemoteAddress().getAddress() != null) {
            return request.getRemoteAddress().getAddress().getHostAddress();
        } else {
            return "unknown";
        }

    }
}
