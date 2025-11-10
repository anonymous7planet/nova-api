package com.nova.anonymousplanet.core.context;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.context
  fileName : UserContextHolder
  author : Jinhong Min
  date : 2025-11-06
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-06      Jinhong Min      최초 생성
  ==============================================
 */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    public static void set(UserContext userContext) {
        CONTEXT.set(userContext);
    }

    public static UserContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
