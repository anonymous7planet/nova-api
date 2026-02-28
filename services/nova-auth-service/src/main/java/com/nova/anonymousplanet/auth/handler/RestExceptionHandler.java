package com.nova.anonymousplanet.auth.handler;

import com.nova.anonymousplanet.web.handler.RestBaseExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.handler
 * fileName : RestExceptionHandler
 * author : Jinhong Min
 * date : 2025-11-13
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-13      Jinhong Min      최초 생성
 * ==============================================
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // 이 핸들러를 최우선으로 적용
public class RestExceptionHandler extends RestBaseExceptionHandler {
}
