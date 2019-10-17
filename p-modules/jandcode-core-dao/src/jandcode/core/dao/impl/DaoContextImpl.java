package jandcode.core.dao.impl;

import jandcode.core.*;
import jandcode.core.dao.*;

public class DaoContextImpl implements DaoContext {

    private App app;
    private BeanFactory beanFactory = new DefaultBeanFactory(this);

    public DaoContextImpl(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof Dao) {
            ((Dao) inst).setContext(this);
        }
    }
}
