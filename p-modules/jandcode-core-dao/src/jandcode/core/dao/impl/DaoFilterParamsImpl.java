package jandcode.core.dao.impl;

import jandcode.core.dao.*;

public class DaoFilterParamsImpl implements DaoFilterParams {

    private DaoContext context;
    private Object daoInst;
    private Object result;
    private Throwable exception;
    private DaoMethodDef daoMethodDef;
    private long startTime;

    public DaoFilterParamsImpl(DaoContext context, Object daoInst, DaoMethodDef daoMethodDef) {
        this.context = context;
        this.daoInst = daoInst;
        this.daoMethodDef = daoMethodDef;
        this.startTime = System.currentTimeMillis();
    }

    public DaoContext getContext() {
        return context;
    }

    public Object getDaoInst() {
        return daoInst;
    }

    public DaoMethodDef getDaoMethodDef() {
        return daoMethodDef;
    }

    public long getStartTime() {
        return startTime;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

}
