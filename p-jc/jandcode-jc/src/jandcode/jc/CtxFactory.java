package jandcode.jc;

import jandcode.commons.error.*;
import jandcode.jc.impl.*;

public class CtxFactory {

    private static Ctx runtimeCtx;

    /**
     * Создание нового экземпляра контекста
     */
    public static Ctx createCtx() {
        return new CtxImpl();
    }

    /**
     * Глобальный ctx, который можно использовать в runtime
     */
    public static Ctx getRuntimeCtx() {
        if (runtimeCtx == null) {
            synchronized (CtxFactory.class) {
                if (runtimeCtx == null) {
                    Ctx ctx = new CtxImpl(true);

                    try {
                        JcConfig cfg = JcConfigFactory.load("");
                        ctx.applyConfig(cfg);
                    } catch (Exception e) {
                        throw new XErrorWrap(e);
                    }

                    runtimeCtx = ctx;
                }
            }
        }
        return runtimeCtx;
    }

}
