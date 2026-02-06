package com.nova.anonymousplanet.persistence.util.crypto;

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
 * fileName : EncryptoUtils
 * author : Jinhong Min
 * date : 2025-12-26
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-26      Jinhong Min      최초 생성
 * ==============================================
 */
public class EncryptionUtils {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128; // 인증 태그 길이
    private static final int IV_LENGTH_BYTE = 12;  // GCM 권장 IV 길이
    private static final int AES_KEY_BIT = 256;    // AES 256비트

    // FIXME: 추후 KEY값 보관 필요
    // 실제 운영 환경에서는 환경 변수나 AWS KMS 등을 통해 주입받아야 합니다.
    // 32바이트(256비트) 키 문자열 예시
    private static final String SECRET_KEY = "12345678901234567890123456789012";

    /**
     * 1. 양방향 암호화 (AES-256-GCM)
     */
    public static String encrypt(String plainText) throws Exception {
        // 1. IV(Initialization Vector) 생성
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv);

        // 2. Cipher 초기화
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        // 3. 암호화 수행
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // 4. IV와 암호문을 합쳐서 Base64 인코딩 (나중에 복호화 때 IV가 필요함)
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);

        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    /**
     * 2. 양방향 복호화
     */
    public static String decrypt(String encryptedText) throws Exception {
        // 1. Base64 디코딩
        byte[] decoded = Base64.getDecoder().decode(encryptedText);

        // 2. IV와 암호문 분리
        ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byteBuffer.get(iv);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        // 3. Cipher 초기화
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        // 4. 복호화 수행
        byte[] decryptedText = cipher.doFinal(cipherText);
        return new String(decryptedText, StandardCharsets.UTF_8);
    }

    /**
     * 3. 검색용 단방향 해시 (SHA-256)
     * 동일한 평문은 항상 동일한 해시값을 가지므로 DB 인덱스 검색이 가능합니다.
     */

    public static String hashForSearch(String plainText) {
        if (plainText == null) return null;
        try {
            String salt = "my-app-specific-salt";
            String target = plainText + salt;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(target.getBytes(StandardCharsets.UTF_8));

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException("해시 생성 중 오류 발생", e);
        }
    }
}
