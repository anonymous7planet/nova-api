package com.nova.anonymousplanet.common.constant;

public enum YesNoCode implements BaseEnum {
    YES("Y", "Yes"),
    NO("N", "No");

    private final String code;
    private final String desc;

    YesNoCode(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
