package com.nova.anonymousplanet.system.controller.v1;

import com.nova.anonymousplanet.core.model.response.NovaResponse;
import com.nova.anonymousplanet.system.dto.v1.CodeResponse;
import com.nova.anonymousplanet.system.dto.v1.CommonCodeResponse;
import com.nova.anonymousplanet.system.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * [1] 시스템 전체 Enum 코드 목록 조회
     * @return 각 카테고리별 Enum 코드 리스트 맵
     */
    @GetMapping("/system")
    public ResponseEntity<NovaResponse<Map<String, CodeResponse.EnumCodeResponse<?>>>> getAllSystemCodes() {
        // 변수명: systemCodeMap (복수형 맵)
        Map<String,CodeResponse.EnumCodeResponse<?>> systemCodes = codeService.getAllSystemCodes();
        return ResponseEntity.ok(NovaResponse.success(systemCodes));
    }

    /**
     * [2] 특정 시스템 Enum 코드 항목 조회
     * @param enumName 조회할 항목명 (예: gender, mbti)
     */
    @GetMapping("/system/{enumName}")
    public ResponseEntity<NovaResponse<CodeResponse.EnumCodeResponse<?>>> getSpecificSystemCodes(@PathVariable String enumName) {
        // 변수명: codes (복수형)
        CodeResponse.EnumCodeResponse<?> codes = codeService.getSpecificSystemCode(enumName);
        return ResponseEntity.ok(NovaResponse.success(codes));
    }


    /**
     * [2] DB 공통 코드 그룹 조회 (트리 구조)
     * 취미, 관심사 등 관리자 페이지에서 수정이 가능한 계층형 데이터를 조회할 때 사용합니다.
     */
    @GetMapping("/group/{groupCode}")
    public ResponseEntity<NovaResponse<List<CommonCodeResponse>>> getGroupCodes(
            @PathVariable String groupCode,
            @RequestHeader(name = "Accept-Language", defaultValue = "ko") String lang) {
        List<CommonCodeResponse> commonCodes = codeService.getCodeTree(groupCode, lang);
        return ResponseEntity.ok(NovaResponse.success(commonCodes));
    }
}
