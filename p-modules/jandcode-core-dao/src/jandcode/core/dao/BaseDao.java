package jandcode.core.dao;

import jandcode.core.*;

/**
 * Базовый предок для dao
 */
public abstract class BaseDao extends BaseComp implements Dao {

    private DaoContext context;

    public App getApp() {
        return getContext().getApp();
    }

    public DaoContext getContext() {
        return context;
    }

    public void setContext(DaoContext context) {
        this.context = context;
    }

}
