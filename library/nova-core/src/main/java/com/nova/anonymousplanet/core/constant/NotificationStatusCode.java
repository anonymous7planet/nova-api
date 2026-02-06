package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : NotificationStatusCode
 * author : Jinhong Min
 * date : 2026-02-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-04      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
@RequiredArgsConstructor
public enum NotificationStatusCode implements BaseEnum<String> {

    PENDING("PENDING", "[발송 대기] 발송 요청이 접수되어 처리를 기다리는 상태"),
    VALIDATION_FAILED("VALIDATION_FAILED", "[검증 실패] 필수 파라미터 누락 등 데이터 유효성 검증 실패"),
    PROCESSING("PROCESSING", "[발송 중] 템플릿 렌더링 및 외부 메일 서버와 통신 중"),
    SUCCESS("SUCCESS", "[발송 성공] 사용자에게 최종적으로 발송 완료"),
    FAILED("FAILED", "[발송 실패] 네트워크 오류, 인증 오류 등 외부 요인으로 인한 실패"),
    RETRYING("RETRYING", "[재시도 중] 일시적 오류로 인해 재발송을 시도하는 상태")

    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }


    @JsonCreator
    public static NotificationStatusCode fromCode(String code) {
        return EnumUtils.fromCode(NotificationStatusCode.class, code);
    }

    @Converter
    public static class NotificationStatusCodeConverter extends BaseEnumConverter<NotificationStatusCode, String> {
        public NotificationStatusCodeConverter() {
            super(NotificationStatusCode.class);
        }
    }
}
