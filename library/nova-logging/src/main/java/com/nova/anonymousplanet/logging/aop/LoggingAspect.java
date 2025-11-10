package com.nova.anonymousplanet.logging.aop;

import com.nova.anonymousplanet.logging.util.TraceIdContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.logging.aop
 * fileName : LoggingAspect
 * author : Jinhong Min
 * date : 2025-11-09
 * description :
 * 공통 AOP 로그 처리 클래스
 * - 요청/응답/예외에 TraceId 포함
 * - JSON-like 포맷으로 출력
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-09      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {}

    @Before("restController()")
    public void logBefore(JoinPoint joinPoint) {
        TraceIdContext.init();
        log.info("[REQUEST] traceId={} method={} args={}",
            TraceIdContext.getTraceId(),
            joinPoint.getSignature().toShortString(),
            Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "restController()", returning = "response")
    public void logAfterReturning(JoinPoint joinPoint, Object response) {
        log.info("[RESPONSE] traceId={} method={} result={}",
            TraceIdContext.getTraceId(),
            joinPoint.getSignature().toShortString(),
            response);
        TraceIdContext.clear();
    }

    @AfterThrowing(value = "restController()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("[ERROR] traceId={} method={} exception={} message={}",
            TraceIdContext.getTraceId(),
            joinPoint.getSignature().toShortString(),
            ex.getClass().getSimpleName(),
            ex.getMessage(), ex);
        TraceIdContext.clear();
    }
}
