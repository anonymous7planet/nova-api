package com.nova.anonymousplanet.system.dto.v1;

import com.nova.anonymousplanet.core.constant.BaseEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


/**
 * 공통 코드 응답을 관리하는 최상위 클래스
 */
public record CodeResponse() {

    /**
     * 코드 그룹 전체의 정보를 담는 Record (타이틀과 하위 리스트 포함)
     */
    public record CommonCodeResponse(
            String title,
            List<EnumCodeResponse> children
    ) {
        /**
         * CommonCodeResponse 생성을 위한 create 함수 (지침 반영)
         */
        public static CommonCodeResponse create(String title, List<EnumCodeResponse> children) {
            return new CommonCodeResponse(
                    title,
                    children != null ? children : new ArrayList<>()
            );
        }
    }

    public record EnumCodeResponse<T>(
            String title, List<EnumCode<T>> codes
    ) {
        public static <T> EnumCodeResponse<T> of(String title, BaseEnum<T>[] enums) {
            return new EnumCodeResponse<>(title, Arrays.stream(enums).map(EnumCode::of).toList());
        }

    }


    private record EnumCode<T>(
            String name, T code, String desc
    ) {
        public static <T> EnumCode<T> of(BaseEnum<T> baseEnum) {
            return new EnumCode<>(baseEnum.getName(), baseEnum.getCode(), baseEnum.getDesc());
        }
    }
}