package com.nova.anonymousplanet.auth.utils;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.utils
 * fileName : KeyGeneratorUtil
 * author : Jinhong Min
 * date : 2026-02-03
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-03      Jinhong Min      최초 생성
 * ==============================================
 */
public class KeyGeneratorUtil {
    private KeyGeneratorUtil(){}

    // AES-256을 위한 32바이트 키 생성 메서드
    public static String generateAes256Key() {
        byte[] key = new byte[32]; // 32 bytes = 256 bits
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public static void main(String[] args) {
        // 실행 시 콘솔에 출력된 값을 application.yml에 복사해서 사용하세요.
        System.out.println("Generated Key: " + generateAes256Key());
    }
}
