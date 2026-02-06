package com.nova.anonymousplanet.notification.provider;

import com.nova.anonymousplanet.core.constant.NotificationTypeCode;
import com.nova.anonymousplanet.notification.dto.v1.SenderDto;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.provider
 * fileName : SenderProvider
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */
public interface NotificationProvider<T> {
    NotificationTypeCode getType();

    /**
     * 실제 발송 처리.
     * 각 매체별로 Entity 구조가 다르므로 최상위 객체인 Object로 전달받아 내부에서 캐스팅합니다.
     */
    void send(SenderDto<T> senderDto);
}
