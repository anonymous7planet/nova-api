package com.nova.anonymousplanet.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : NovaEventType
 * author : Jinhong Min
 * date : 2026-02-03
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-03      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
@RequiredArgsConstructor
public enum NovaEventType {

    COMMON_CODE_CHANGED("common-code-topic", "공통 코드 변경 이벤트"),
    USER_STATUS_CHANGED("user-topic", "사용자 상태 변경 이벤트")
    ;

    private final String topic;
    private final String description;
}
