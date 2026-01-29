package com.nova.anonymousplanet.gateway.logging;

import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import lombok.NonNull;
import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.logging
 * fileName : ReactorMdcHook
 * author : Jinhong Min
 * date : 2026-01-27
 * description : 
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-27      Jinhong Min      최초 생성
 * ==============================================
 */
public final class ReactorMdcHook {

    private ReactorMdcHook() {}

    public static void install() {

        Hooks.onEachOperator("mdc",
                Operators.lift((sc, sub) -> new CoreSubscriber<>() {

                    @Override
                    public Context currentContext() {
                        return sub.currentContext();
                    }

                    @Override
                    public void onSubscribe(Subscription s) {
                        sub.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Object o) {
                        syncMdc();
                        sub.onNext(o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        syncMdc();
                        sub.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        syncMdc();
                        sub.onComplete();
                    }

                    private void syncMdc() {
                        MDC.clear();
                        Context ctx = currentContext();
                        for (LogContextCode code : LogContextCode.values()) {
                            if (code.isMdcSupport() && code.getMdcKey() != null) {
                                String v = ctx.getOrDefault(code.getMdcKey(), null);
                                if (v != null) {
                                    MDC.put(code.getMdcKey(), v);
                                }
                            }
                        }
                    }
                })
        );
    }
}
