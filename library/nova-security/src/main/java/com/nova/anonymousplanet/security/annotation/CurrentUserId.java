package com.nova.anonymousplanet.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.annotation
 * fileName : CurrentUserId
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * 컨트롤러 파라미터에서 현재 사용자의 내부 Long ID를 주입받을 때 사용합니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
@Target(ElementType.PARAMETER)      // 파라미터 위치에만 사용 가능하도록 제한
@Retention(RetentionPolicy.RUNTIME)  // 런타임까지 어노테이션 정보 유지 (Spring이 인식해야 함)
public @interface CurrentUserId {
}