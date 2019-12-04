package jandcode.core.dao.impl;

import jandcode.core.dao.*;

public class DaoFilterParamsImpl implements DaoFilterParams {

    private DaoContext context;
    private Object daoInst;
    private Object result;
    private Throwable exception;

    public DaoFilterParamsImpl(DaoContext context, Object daoInst) {
        this.context = context;
        this.daoInst = daoInst;
    }

    public DaoContext getContext() {
        return context;
    }

    public Object getDaoInst() {
        return daoInst;
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
