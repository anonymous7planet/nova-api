package com.nova.anonymousplanet.auth.provider.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.converter
 * fileName : CryptoConverter
 * author : Jinhong Min
 * date : 2026-01-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-06      Jinhong Min      최초 생성
 * ==============================================
 */
@Converter
@RequiredArgsConstructor
public class CryptoConverter implements AttributeConverter<String, String> {

    private final EncryptionProvider encryptionProvider;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptionProvider.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptionProvider.decrypt(dbData);
    }
}
