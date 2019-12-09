package jandcode.core.dao.impl;

import jandcode.core.dao.*;

public class DaoHolderItemImpl implements DaoHolderItem {

    private String name;
    private DaoMethodDef methodDef;
    private String daoManagerName;

    public DaoHolderItemImpl(String name, DaoMethodDef methodDef, String daoManagerName) {
        this.name = name;
        this.methodDef = methodDef;
        this.daoManagerName = daoManagerName;
    }

    public String getName() {
        return name;
    }

    public DaoMethodDef getMethodDef() {
        return methodDef;
    }

    public String getDaoManagerName() {
        return daoManagerName;
    }
}
