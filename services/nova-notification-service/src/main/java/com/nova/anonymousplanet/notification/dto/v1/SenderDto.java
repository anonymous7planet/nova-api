package com.nova.anonymousplanet.notification.dto.v1;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.notification.dto.v1
 * fileName : SenderDto
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */
public record SenderDto<T>(
        Long historyId,    // 내부 로직 처리용 (Long ID)
        String externalId, // 외부 노출/추적용 (UUID)
        T payload                // 매체별 상세 데이터 (EmailPayload 등)
) {
}
