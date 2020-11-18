package jandcode.core.dao.impl;

import jandcode.core.dao.*;

public class DaoHolderItemImpl implements DaoHolderItem {

    private String name;
    private DaoMethodDef methodDef;
    private DaoHolderImpl daoHolder;

    public DaoHolderItemImpl(DaoHolderImpl daoHolder, String name, DaoMethodDef methodDef) {
        this.daoHolder = daoHolder;
        this.name = name;
        this.methodDef = methodDef;
    }

    public String getName() {
        return name;
    }

    public DaoMethodDef getMethodDef() {
        return methodDef;
    }

    public String getDaoInvokerName() {
        return this.daoHolder.resolveDaoInvokerName(getName());
    }

}
