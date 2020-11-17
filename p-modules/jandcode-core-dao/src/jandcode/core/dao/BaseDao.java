package jandcode.core.dao;

import jandcode.commons.error.*;
import jandcode.core.*;

/**
 * Базовый предок для dao
 */
public abstract class BaseDao extends BaseComp implements Dao {

    private DaoContext context;

    public DaoContext getContext() {
        if (context == null) {
            throw new XError("Доступ к context для dao вне вызова метода dao");
        }
        return context;
    }

    public void setContext(DaoContext context) {
        this.context = context;
    }

    protected boolean hasContext() {
        return this.context != null;
    }

}
