package com.nova.anonymousplanet.security.xss;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.xss
 * fileName : HtmlCharacterEscapes
 * author : Jinhong Min
 * date : 2026-02-23
 * description :
 * Jackson 직렬화 시 XssUtils.escape 를 적용하는 규칙
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-23      Jinhong Min      최초 생성
 * ==============================================
 */
public class HtmlCharacterEscapes extends CharacterEscapes {
    private final int[] asciiEscapes;

    public HtmlCharacterEscapes() {
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    @Override
    public int[] getEscapeCodesForAscii() { return asciiEscapes; }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        return new SerializedString(XssUtils.escape(Character.toString((char) ch)));
    }
}
