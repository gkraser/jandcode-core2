package jandcode.jc;

import jandcode.jc.impl.*;

public class CtxFactory {

    /**
     * Создание нового экземпляра контекста
     */
    public static Ctx createCtx() {
        return new CtxImpl();
    }

}
