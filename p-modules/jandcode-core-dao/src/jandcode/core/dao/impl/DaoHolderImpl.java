package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dao.*;

import java.util.*;

public class DaoHolderImpl extends BaseComp implements DaoHolder {

    private NamedList<DaoHolderItem> items;
    private String daoManagerName;

    public DaoHolderImpl() {
        this.items = new DefaultNamedList<>();
        this.items.setNotFoundMessage("Не найден dao-метод {0} в dao-хранилище {1}", this);
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
    }

    public Object invokeDao(String name, Object... args) throws Exception {
        DaoHolderItem d = items.get(name);
        String dmn = d.getDaoManagerName();
        if (UtString.empty(dmn)) {
            dmn = getDaoManagerName();
        }
        DaoManager dm = getApp().bean(DaoService.class).getDaoManager(dmn);
        return dm.invokeDao(d.getMethodDef(), args);
    }

    public Collection<DaoHolderItem> getItems() {
        return items;
    }

    public String getDaoManagerName() {
        if (UtString.empty(daoManagerName)) {
            return "default";
        }
        return daoManagerName;
    }

    public void setDaoManagerName(String daoManagerName) {
        this.daoManagerName = daoManagerName;
    }

}
