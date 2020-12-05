package jandcode.core.dao.impl;

import jandcode.core.dao.*;

public class DaoHolderItemImpl implements DaoHolderItem {

    private String name;
    private DaoMethodDef methodDef;
    private String daoInvokerName;

    public DaoHolderItemImpl(String name, DaoMethodDef methodDef, String daoInvokerName) {
        this.name = name;
        this.methodDef = methodDef;
        this.daoInvokerName = daoInvokerName;
    }

    public String getName() {
        return name;
    }

    public DaoMethodDef getMethodDef() {
        return methodDef;
    }

    public String getDaoInvokerName() {
        return daoInvokerName;
    }

}
