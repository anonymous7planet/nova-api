package com.nova.anonymousplanet.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.annotation
 * fileName : CurrentUserUserInfo
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * 컨트롤러 파라미터에서 현재 사용자의 상세 정보(ID, UUID, Role) 객체를 주입받을 때 사용합니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserUserInfo {
}
