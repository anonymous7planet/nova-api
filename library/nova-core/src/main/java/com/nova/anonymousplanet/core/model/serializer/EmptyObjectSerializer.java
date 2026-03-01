package com.nova.anonymousplanet.core.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.dto.v1.response
 * fileName : EmptyObjectSerializer
 * author : Jinhong Min
 * date : 2026-01-20
 * description :
 * Null 데이터를 빈 JSON 객체({})로 변환하는 시리얼라이저
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-20      Jinhong Min      최초 생성
 * ==============================================
 */

public class EmptyObjectSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // null 값이 들어오거나 명시적으로 호출될 경우 {}를 작성
        gen.writeStartObject();
        gen.writeEndObject();
    }
}
