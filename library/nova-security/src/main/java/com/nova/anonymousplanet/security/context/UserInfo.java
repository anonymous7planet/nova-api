package com.nova.anonymousplanet.security.context;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.context
 * fileName : UserINfo
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
public record UserInfo(
        Long userId,
        String userUuid,
        UserRoleCode userRole
) {

    public boolean isSystem() {
        return userRole == UserRoleCode.SYSTEM;
    }

    public boolean isAdmin() {
        return userRole == UserRoleCode.ADMIN || userRole == UserRoleCode.MANAGER;
    }

    public boolean isUser() {
        return userRole == UserRoleCode.USER;
    }
}
