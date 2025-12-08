package com.nova.anonymousplanet.core.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
}
