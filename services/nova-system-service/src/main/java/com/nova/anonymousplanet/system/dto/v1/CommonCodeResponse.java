package com.nova.anonymousplanet.system.dto.v1;

import com.nova.anonymousplanet.system.entity.CommonCodeEntity;

import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.dto
 * fileName : CommonCodeResponse
 * author : Jinhong Min
 * date : 2026-01-09
 * description : 공통 코드 반환
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-09      Jinhong Min      최초 생성
 * ==============================================
 */
public record CommonCodeResponse(
        String codeId,
        String codeName,
        List<CommonCodeResponse> children
) {
    /**
     * 언어 설정을 반영하여 엔티티를 DTO로 변환
     */
    public static CommonCodeResponse from(CommonCodeEntity entity, String lang) {
        return new CommonCodeResponse(
                entity.getCodeId(),
                entity.getLocaleName(lang),
                entity.getChildren().stream()
                        .filter(c -> "Y".equals(c.getIsUsed()) && "Y".equals(c.getIsDisplay()))
                        .map(c -> from(c, lang))
                        .toList()
        );
    }
}
