package com.nova.anonymousplanet.system.service;

import com.nova.anonymousplanet.core.constant.*;
import com.nova.anonymousplanet.system.dto.v1.CodeResponse;
import com.nova.anonymousplanet.system.dto.v1.CommonCodeResponse;
import com.nova.anonymousplanet.system.entity.CommonCodeEntity;
import com.nova.anonymousplanet.system.repository.CommonCodeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    // 서버 기동 시점에 한 번만 생성되어 메모리에 상주 (가장 빠름)
    private static final Map<String, CodeResponse.EnumCodeResponse<?>> SYSTEM_CODE_MAP = new ConcurrentHashMap<>();

    @PostConstruct // 의존성 주입 후 자동 실행
    public void init() {
        SYSTEM_CODE_MAP.put("bloodType", toResponseList("혈액형", BloodTypeCode.values()));
        SYSTEM_CODE_MAP.put("educationLevel", toResponseList("교육수준", EducationLevelCode.values()));
        SYSTEM_CODE_MAP.put("gender", toResponseList("성별", GenderCode.values()));
        SYSTEM_CODE_MAP.put("jobCategory", toResponseList("직업카테고리", JobCategoryCode.values()));
        SYSTEM_CODE_MAP.put("language", toResponseList("언어", LanguageCode.values()));
        SYSTEM_CODE_MAP.put("loginProvider", toResponseList("로그인방법", LoginProviderCode.values()));
        SYSTEM_CODE_MAP.put("mbti", toResponseList("MBTI", MbtiCode.values()));
        SYSTEM_CODE_MAP.put("religion", toResponseList("종교", ReligionCode.values()));
        SYSTEM_CODE_MAP.put("serviceMode", toResponseList("서비스모드", ServiceModeCode.values()));
        SYSTEM_CODE_MAP.put("yesNo", toResponseList("Yes/No", YesNoCode.values()));
    }

    public Map<String, CodeResponse.EnumCodeResponse<?>> getAllSystemCodes() {
        return SYSTEM_CODE_MAP;
    }

    public CodeResponse.EnumCodeResponse<?> getSpecificSystemCode(String enumName) {
        return SYSTEM_CODE_MAP.getOrDefault(enumName, null);
    }

    private <T> CodeResponse.EnumCodeResponse<T> toResponseList(String title, BaseEnum<T>[] enums) {
        return CodeResponse.EnumCodeResponse.of(title, enums);
    }




    /**
     * 그룹 코드를 기반으로 최상위 코드 리스트를 조회합니다.
     * (children 필드에 의해 하위 트리까지 JPA가 로드합니다.)
     */
    @Cacheable(value = "commonCodeTree", key = "#groupCode")
    public List<CommonCodeResponse> getCodeTree(String groupCode, String lang) {
        List<CommonCodeEntity> entities = commonCodeRepository.findRootCodesByGroupCode(groupCode);

        // 트랜잭션이 유지되는 여기서 children에 접근하므로 에러가 나지 않습니다.
        return entities.stream()
                .map(e -> CommonCodeResponse.from(e, lang))
                .toList();
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
