package jandcode.dao;

import jandcode.core.*;

/**
 * Базовый предок для dao
 */
public abstract class BaseDao extends BaseComp implements Dao {

    private DaoContext context;

    public DaoContext getContext() {
        return context;
    }

    public void setContext(DaoContext context) {
        this.context = context;
    }

}
