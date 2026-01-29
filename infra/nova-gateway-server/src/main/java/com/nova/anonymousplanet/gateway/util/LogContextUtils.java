package com.nova.anonymousplanet.gateway.util;

import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.util.context.Context;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.util
 * fileName : LogContextUrila
 * author : Jinhong Min
 * date : 2026-01-26
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-26      Jinhong Min      최초 생성
 * ==============================================
 */
public class LogContextUtils {

    private LogContextUtils() {}

    public static Context populateContext(ServerHttpRequest request) {
       Context ctx = Context.empty();

       for(LogContextCode code : LogContextCode.values()) {
           if(!code.isMdcSupport()) continue;
           if(code.getMdcKey() == null) continue;

           String value = request.getHeaders().getFirst(code.getHeaderKey());
           if(value != null && !value.isBlank()) {
               ctx = ctx.put(code.getMdcKey(), value);
           }
       }
       return ctx;
    }
}
