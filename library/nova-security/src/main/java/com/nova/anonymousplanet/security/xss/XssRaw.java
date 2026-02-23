package com.nova.anonymousplanet.security.xss;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.util
 * fileName : XssRaw
 * author : Jinhong Min
 * date : 2026-02-23
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-23      Jinhong Min      최초 생성
 * ==============================================
 */

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 내부 통신이나 원본 데이터가 필요할 때 사용하는 한정자
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface XssRaw {
}
