package jandcode.core.dao;

import jandcode.core.*;

/**
 * Предок для фильтров dao
 */
public abstract class BaseDaoFilter extends BaseComp implements DaoFilter, IDaoManagerLink, IDaoManagerLinkSet {

    private DaoManager daoManager;

    public DaoManager getDaoManager() {
        return daoManager;
    }

    public void setDaoManager(DaoManager daoManager) {
        this.daoManager = daoManager;
    }

    public void beforeInvoke(DaoFilterParams p) {
    }

    public void afterInvoke(DaoFilterParams p) {
    }

    public void errorInvoke(DaoFilterParams p) {
    }

}
