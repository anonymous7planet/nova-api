package com.nova.anonymousplanet.core.util;

import java.util.UUID;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.util
  fileName : UuidUtils
  author : Jinhong Min
  date : 2025-10-28
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-28      Jinhong Min      최초 생성
  ==============================================
 */
public class UuidUtils {

    private UuidUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 표준 UUID (하이픈 포함)
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }

    /**
     * 하이픈 제거 UUID
     */
    public static String generateWithoutHyphen() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Prefix가 붙은 UUID (예: USER_abcde-1234...)
     */
    public static String generateWithPrefix(String prefix) {
        return prefix + "_" + UUID.randomUUID();
    }
}
