package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum GenderCode implements BaseEnum<String> {

    MALE("M", "남성"),
    FEMALE("F", "여성"),
    OTHER("O", "기타")
    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static GenderCode fromCode(String code) {
        return EnumUtils.fromCode(GenderCode.class, code);
    }
}
