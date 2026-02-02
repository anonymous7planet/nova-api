package com.nova.anonymousplanet.security.resolver;

import com.nova.anonymousplanet.security.annotation.CurrentUserId;
import com.nova.anonymousplanet.security.annotation.CurrentUserUserInfo;
import com.nova.anonymousplanet.security.context.UserContext;
import com.nova.anonymousplanet.security.context.UserInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.resolver
 * fileName : NovaUserArgumentResolver
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
public class NovaUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class) ||
                parameter.hasParameterAnnotation(CurrentUserUserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        UserInfo userInfo = UserContext.getUserInfo();
        if (userInfo == null) return null;

        if (parameter.hasParameterAnnotation(CurrentUserId.class)) {
            return userInfo.userId();
        }

        return userInfo; // @CurrentUserUserInfo인 경우 객체 반환
    }
}
