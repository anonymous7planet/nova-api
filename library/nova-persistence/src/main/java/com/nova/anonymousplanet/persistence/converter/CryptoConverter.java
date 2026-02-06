package com.nova.anonymousplanet.persistence.converter;

import com.nova.anonymousplanet.persistence.util.crypto.EncryptionUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.persistence.converter
 * fileName : CryptoConverter
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            return EncryptionUtils.encrypt(attribute);
        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return EncryptionUtils.decrypt(dbData);
        } catch (Exception e) {
            return dbData;
        }
    }
}
