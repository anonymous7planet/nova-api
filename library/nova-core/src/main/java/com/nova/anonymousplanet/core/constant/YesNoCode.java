package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum YesNoCode implements BaseEnum<String> {
    YES("Y", "Yes"),
    NO("N", "No");

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static YesNoCode fromCode(String code) {
        return EnumUtils.fromCode(YesNoCode.class, code);
    }
}
