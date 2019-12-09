package jandcode.core.dao.impl;

import jandcode.core.*;
import jandcode.core.dao.*;

public class DaoContextImpl implements DaoContext, IBeanIniter {

    private App app;
    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    private long startTime;

    public DaoContextImpl(App app) {
        this.app = app;
        // время начала выполнения dao
        this.startTime = System.currentTimeMillis();
    }

    public App getApp() {
        return app;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IDaoContextLinkSet) {
            ((IDaoContextLinkSet) inst).setContext(this);
        }
    }

    //////

    public long getStartTime() {
        return startTime;
    }

}
