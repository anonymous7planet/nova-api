package com.nova.anonymousplanet.security.context;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.context
 * fileName : UserContext
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
public class UserContext {
    private static final ThreadLocal<UserInfo> USER_INFO_HOLDER = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        USER_INFO_HOLDER.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return USER_INFO_HOLDER.get();
    }

    public static Long getUserId() {
        UserInfo info = USER_INFO_HOLDER.get();
        return (info != null) ? info.userId() : null;
    }

    public static void clear() {
        USER_INFO_HOLDER.remove();
    }
}
