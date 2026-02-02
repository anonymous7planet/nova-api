package com.nova.anonymousplanet.security.feign;

import com.nova.anonymousplanet.security.constant.HeaderContextCode;
import com.nova.anonymousplanet.security.context.UserContext;
import com.nova.anonymousplanet.security.context.UserInfo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.feign
 * fileName : NovaFeignInterceptor
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * 서비스 간 직접 호출 시 모든 보안 문맥(ID, UUID, Role)을 전파
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
@RequiredArgsConstructor
public class NovaFeignInterceptor implements RequestInterceptor {

    private final String gatewaySecret;
    private final String clientName; // spring.application.name


    @Override
    public void apply(RequestTemplate template) {
        template.header(HeaderContextCode.GATEWAY_SECRET.getHeaderKey(), gatewaySecret);
        template.header(HeaderContextCode.SERVICE_NAME.getHeaderKey(), clientName);

        UserInfo userInfo = UserContext.getUserInfo();
        if (userInfo != null) {
            template.header(HeaderContextCode.USER_ID.getHeaderKey(), String.valueOf(userInfo.userId()));
            if (userInfo.userUuid() != null) {
                template.header(HeaderContextCode.USER_UUID.getHeaderKey(), userInfo.userUuid());
            }
            if (userInfo.userRole() != null) {
                template.header(HeaderContextCode.USER_ROLE.getHeaderKey(), userInfo.userRole().getCode());
            }
        }
    }
}
