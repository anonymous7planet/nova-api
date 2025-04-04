package com.nova.anonymousplanet.common.configuration;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nova.anonymousplanet.common.constant.BaseEnum;

import java.io.IOException;


/**
 * Response에서 Enum객체가 아니라 Code값만 반환
 * @JsonSerialize(using = BaseEnumSerializer.class)
 */
public class EnumSerializer extends JsonSerializer<BaseEnum> {

    @Override
    public void serialize(BaseEnum value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeString(value.getCode());
    }
}
