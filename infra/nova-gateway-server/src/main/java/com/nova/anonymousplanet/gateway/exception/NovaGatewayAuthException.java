package com.nova.anonymousplanet.gateway.exception;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.exception
 * fileName : GatewayAuthException
 * author : Jinhong Min
 * date : 2025-11-15
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-15      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
public class NovaGatewayAuthException extends AuthenticationException {

    private final ErrorCode errorCode; // 에러 객체 자체를 보관
    private final String customMessage;

    public NovaGatewayAuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = errorCode.getMessage();
    }

    public NovaGatewayAuthException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

}
