package com.nova.anonymousplanet.core.util;

import com.nova.anonymousplanet.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : PathUtils
 * author : Jinhong Min
 * date : 2026-03-08
 * description :
 * [Project Nova] 전사 공통 경로 검증 유틸리티
 * AntPathMatcher의 캐싱 기능을 활성화하여 고성능 경로 매칭을 지원합니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-08      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
public class PathUtils {
    // 외부 인스턴스화 방지
    private PathUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Bill Pugh Singleton Holder
     * Lazy Loading과 Thread-safety를 보장하며, 최적화된 인스턴스를 유지합니다.
     */
    private static class AntPathMatcherHolder {
        private static final AntPathMatcher INSTANCE;

        static {
            INSTANCE = new AntPathMatcher();
            // 캐싱 활성화: 대규모 트래픽에서 경로 파싱 성능을 비약적으로 향상시킵니다.
            INSTANCE.setCachePatterns(true);
            INSTANCE.setCaseSensitive(true); // 프로젝트 규칙에 따라 대소문자 구분
        }
    }

    /**
     * @return AntPathMatcher 싱글톤 인스턴스
     */
    public static AntPathMatcher getMatcher() {
        return AntPathMatcherHolder.INSTANCE;
    }

    /**
     * 일반적인 경로 매칭 확인
     * @param pattern Ant 패턴 (예: /api/v1/**)
     * @param path    실제 경로
     * @return 매칭 여부
     */
    public static boolean match(String pattern, String path) {
        if (!StringUtils.hasText(pattern) || !StringUtils.hasText(path)) {
            return false;
        }
        return getMatcher().match(pattern, path);
    }

    /**
     * [추가 유틸] 경로에서 서비스 명 추출 (예: /api/auth/v1 -> auth)
     */
    public static String extractServiceName(String path) {
        if (!StringUtils.hasText(path) || !path.startsWith("/api/")) {
            return "unknown";
        }
        String[] parts = path.split("/");
        return parts.length > 2 ? parts[2] : "unknown";
    }
}
