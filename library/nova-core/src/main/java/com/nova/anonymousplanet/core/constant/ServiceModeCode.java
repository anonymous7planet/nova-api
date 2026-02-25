package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : ServiceModeCode
 * author : Jinhong Min
 * date : 2026-01-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-06      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
@RequiredArgsConstructor
public enum ServiceModeCode implements BaseEnum<String> {
    NONE("NONE", "미승인 유저"),
    FRIEND("FRIEND", "친구 모드(승인완료)"),
    MATCH_PENDING("MATCH_PENDING", "맞선 모드 승인 대기"),
    MATCH("MATCH", "맞선 모드(최종 승인완료)");

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static ServiceModeCode fromCode(String code) {
        return EnumUtils.fromCode(ServiceModeCode.class, code);
    }
}
