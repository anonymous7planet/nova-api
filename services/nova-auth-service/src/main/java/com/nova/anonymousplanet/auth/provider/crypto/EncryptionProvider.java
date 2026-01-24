package com.nova.anonymousplanet.auth.provider.crypto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util.crypto
 * fileName : EncryptionProvider
 * author : Jinhong Min
 * date : 2026-01-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-06      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
public class EncryptionProvider {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    private final SecretKey secretKey;
    private final String hashSalt;

    public EncryptionProvider(
            @Value("${app.encryption.key}") String key,
            @Value("${app.encryption.salt}") String salt
    ) {
        if (key == null || key.getBytes(StandardCharsets.UTF_8).length != 32) {
            throw new IllegalArgumentException("AES-256 키는 정확히 32바이트여야 합니다.");
        }
        this.secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        this.hashSalt = salt;
    }

    // 양방향 암호화
    public String encrypt(String plainText) {
        if (plainText == null) return null;
        try {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);

            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    // 양방향 복호화
    public String decrypt(String encryptedText) {
        if (encryptedText == null) return null;
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

            byte[] iv = new byte[IV_LENGTH_BYTE];
            byteBuffer.get(iv);
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);

            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

    // 검색용 해시 생성
    public String hashForSearch(String plainText) {
        if (plainText == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(hashSalt.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) hexString.append(String.format("%02x", b));
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash failed", e);
        }
    }
}
