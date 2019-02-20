package jandcode.jc.impl;

import jandcode.commons.error.*;
import jandcode.jc.*;

public class CtxServiceImpl implements ICtxService {

    private Ctx ctx;
    private boolean doCreateExecuted;

    public void setCtx(Ctx ctx) {
        this.ctx = ctx;
    }

    public Ctx getCtx() {
        return ctx;
    }

    //////

    public final void doCreateThis() {
        if (doCreateExecuted) {
            throw new XError("doCreateThis для сервиса можно выполнить только 1 раз");
        }
        doCreateExecuted = true;
        try {
            onCreate();
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    /**
     * Реализация создания сервиса
     */
    protected void onCreate() throws Exception {
    }


}
