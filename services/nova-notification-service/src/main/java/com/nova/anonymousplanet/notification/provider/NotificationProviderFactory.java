package com.nova.anonymousplanet.notification.provider;

import com.nova.anonymousplanet.core.constant.NotificationTypeCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.provider
 * fileName : NotificationProviderFactory
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */

@Component
@RequiredArgsConstructor
public class NotificationProviderFactory {
    private final List<NotificationProvider<?>> providers;

    /**
     * 특정 타입의 프로바이더를 타입 안전하게 찾아 반환합니다.
     */
    @SuppressWarnings("unchecked") // Factory 내부에서만 제한적으로 사용
    public <T> NotificationProvider<T> getProvider(NotificationTypeCode type) {
        return (NotificationProvider<T>) providers.stream()
                .filter(p -> p.getType() == type)
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("지원하지 않는 발송 타입: " + type));
    }
}
