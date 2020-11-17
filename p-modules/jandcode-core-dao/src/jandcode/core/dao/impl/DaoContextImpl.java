package jandcode.core.dao.impl;

import jandcode.core.*;
import jandcode.core.dao.*;

public class DaoContextImpl implements DaoContext, IBeanIniter {

    private BeanFactory beanFactory = new DefaultBeanFactory(this);

    private Object daoInst;
    private Object result;
    private Throwable exception;
    private DaoMethodDef daoMethodDef;
    private DaoInvoker daoInvoker;
    private long startTime;

    public DaoContextImpl(DaoInvoker daoInvoker, DaoMethodDef daoMethodDef) {
        this.daoInvoker = daoInvoker;
        this.daoMethodDef = daoMethodDef;
        this.startTime = System.currentTimeMillis();
    }

    public App getApp() {
        return this.daoInvoker.getApp();
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IDaoContextLinkSet) {
            ((IDaoContextLinkSet) inst).setContext(this);
        }
    }

    public Object getDaoInst() {
        return daoInst;
    }

    public void setDaoInst(Object daoInst) {
        this.daoInst = daoInst;
    }

    public DaoMethodDef getDaoMethodDef() {
        return daoMethodDef;
    }

    public DaoInvoker getDaoInvoker() {
        return daoInvoker;
    }

    public void setDaoInvoker(DaoInvoker daoInvoker) {
        this.daoInvoker = daoInvoker;
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
