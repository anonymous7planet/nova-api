package com.nova.anonymousplanet.system.dto.v1;

import com.nova.anonymousplanet.core.constant.BaseEnum;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.dto
 * fileName : CodeResponse
 * author : Jinhong Min
 * date : 2026-01-07
 * description : Enum 코드 반환
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-07      Jinhong Min      최초 생성
 * ==============================================
 */
public record CodeResponse<T> (T code, String desc) {
    public static <T> CodeResponse of(BaseEnum<T> baseEnum) {
        return new CodeResponse(baseEnum.getCode(), baseEnum.getDesc());
    }
}
