package com.nova.anonymousplanet.system.service;

import com.nova.anonymousplanet.core.entity.CommonCodeEntity;
import com.nova.anonymousplanet.system.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.service.sender.v1
 * fileName : CodeService
 * author : Jinhong Min
 * date : 2026-01-09
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-09      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeService {

    private final CommonCodeRepository commonCodeRepository;

    /**
     * 그룹 코드를 기반으로 최상위 코드 리스트를 조회합니다.
     * (children 필드에 의해 하위 트리까지 JPA가 로드합니다.)
     */
    @Cacheable(value = "commonCodeTree", key = "#groupCode")
    public List<CommonCodeEntity> getCodeTreeEntity(String groupCode) {
        return commonCodeRepository.findRootCodesByGroupCode(groupCode);
    }

    /**
     * 코드 ID와 언어 설정을 받아 해당 언어의 명칭을 반환합니다.
     * * @param codeId 조회할 코드 ID (예: H_SOCCER)
     * @param lang   언어 코드 (ko, en, ja, zh)
     * @return 해당 언어의 코드 명칭
     */
    @Cacheable(value = "commonCodeName", key = "#codeId + '_' + #lang")
    public String getCodeName(String codeId, String lang) {
        return commonCodeRepository.findByCodeId(codeId)
                .map(entity -> entity.getLocaleName(lang))
                .orElse(codeId); // 코드가 없으면 ID 자체를 반환하여 에러 방지
    }

    /**
     * [참고] 만약 캐시를 사용하지 않고 직접 엔티티가 필요한 경우
     */
    public CommonCodeEntity getEntity(String codeId) {
        return commonCodeRepository.findById(codeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코드입니다: " + codeId));
    }


}
