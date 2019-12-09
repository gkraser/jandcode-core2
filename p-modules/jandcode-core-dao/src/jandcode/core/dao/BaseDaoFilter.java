package jandcode.core.dao;

import jandcode.core.*;

/**
 * Предок для фильтров dao
 */
public abstract class BaseDaoFilter extends BaseComp implements DaoFilter, IDaoInvokerLink, IDaoInvokerLinkSet {

    private DaoInvoker daoInvoker;

    public DaoInvoker getDaoInvoker() {
        return daoInvoker;
    }

    public void setDaoInvoker(DaoInvoker daoInvoker) {
        this.daoInvoker = daoInvoker;
    }

    public void beforeInvoke(DaoFilterParams p) {
    }

    public void afterInvoke(DaoFilterParams p) {
    }

    public void errorInvoke(DaoFilterParams p) {
    }

}
