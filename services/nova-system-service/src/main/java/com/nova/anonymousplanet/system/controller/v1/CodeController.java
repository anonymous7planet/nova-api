package com.nova.anonymousplanet.system.controller.v1;

import com.nova.anonymousplanet.core.constant.*;
import com.nova.anonymousplanet.system.dto.v1.CodeResponse;
import com.nova.anonymousplanet.system.dto.v1.CommonCodeResponse;
import com.nova.anonymousplanet.core.entity.CommonCodeEntity;
import com.nova.anonymousplanet.system.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.controller
 * fileName : CommonCodeController
 * author : Jinhong Min
 * date : 2026-01-07
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-07      Jinhong Min      최초 생성
 * ==============================================
 */
@RestController
@RequestMapping("/v1/code")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;


    /**
     * [1] 시스템 고정 Enum 코드 조회
     * 주로 가입 폼의 성별, MBTI 등 절대 변하지 않는 리스트를 한꺼번에 내려줄 때 사용합니다.
     */
    @GetMapping("/system")
    public ResponseEntity<Map<String, List<CodeResponse>>> getAllCodes() {
        Map<String, List<CodeResponse>> codes = new HashMap<>();

        codes.put("religion", Arrays.stream(ReligionCode.values()).map(CodeResponse::of).toList());
        codes.put("education", Arrays.stream(EduCationLevelCode.values()).map(CodeResponse::of).toList());
        codes.put("jobCategory", Arrays.stream(JobCategoryCode.values()).map(CodeResponse::of).toList());
        codes.put("gender", Arrays.stream(GenderCode.values()).map(CodeResponse::of).toList());
        codes.put("mbti", Arrays.stream(MbtiCode.values()).map(CodeResponse::of).toList());

        return ResponseEntity.ok(codes);
    }

    /**
     * [2] DB 공통 코드 그룹 조회 (트리 구조)
     * 취미, 관심사 등 관리자 페이지에서 수정이 가능한 계층형 데이터를 조회할 때 사용합니다.
     */
    @GetMapping("/group/{groupCode}")
    public ResponseEntity<List<CommonCodeResponse>> getGroupCodes(
            @PathVariable String groupCode,
            @RequestHeader(name = "Accept-Language", defaultValue = "ko") String lang) {

        List<CommonCodeEntity> entities = codeService.getCodeTreeEntity(groupCode);

        return ResponseEntity.ok(entities.stream()
                .map(e -> CommonCodeResponse.from(e, lang))
                .toList());
    }

    /**
     * [3] 특정 코드의 명칭 조회 (단건)
     */
    @GetMapping("/{codeId}/name")
    public ResponseEntity<String> getCodeName(
            @PathVariable String codeId,
            @RequestHeader(name = "Accept-Language", defaultValue = "ko") String lang) {

        // Service에 getCodeName(codeId, lang) 메서드 추가 필요
        return ResponseEntity.ok(codeService.getCodeName(codeId, lang));
    }
}
