package com.nova.anonymousplanet.core.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncBaseExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        // 예외 발생한 메서드의 이름과 예외 메시지를 로그에 기록
        log.error("Exception occurred in async method: {}", method.getName());
        log.error("Exception message: {}", throwable.getMessage());

        // 예외 스택 트레이스를 로그에 기록 (더 자세한 정보 제공)
        for (StackTraceElement element : throwable.getStackTrace()) {
            log.error("at " + element.toString());
        }

        // 메서드 인자들이 있다면, 인자들에 대한 로그도 기록
        if (params.length > 0) {
            StringBuilder paramsString = new StringBuilder("Method parameters: ");
            for (Object param : params) {
                paramsString.append(param).append(", ");
            }
            // 마지막 쉼표 제거
            paramsString.setLength(paramsString.length() - 2);
            log.error(paramsString.toString());
        }

        // 예외 처리 후 필요한 후속 작업 (예: 관리자에게 이메일 알림, 알림 서비스 호출 등)
        sendErrorNotification(throwable, method, params);
    }

    /**
     * 예외 발생 시 알림을 보내는 메서드 (예시: 이메일 발송, 외부 시스템 알림 등)
     */
    private void sendErrorNotification(Throwable throwable, Method method, Object[] params) {
        // 예외 발생 정보를 바탕으로 알림을 전송하는 로직 작성
        // 예를 들어 이메일, SMS, Slack 등으로 알림을 전송할 수 있음

        // 이메일 발송 예시
        String errorMessage = String.format("Exception occurred in method: %s, message: %s", method.getName(), throwable.getMessage());
        // 이메일 발송 코드 (예시)
        // emailService.sendErrorNotification(errorMessage, throwable.getStackTrace());
        log.error("Error notification sent: {}", errorMessage);

        // 필요 시, 추가적인 시스템 로그나 다른 서비스와 연동 가능
    }
}
