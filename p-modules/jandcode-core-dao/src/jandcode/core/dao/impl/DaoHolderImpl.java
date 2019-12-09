package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.dao.*;

import java.util.*;

public class DaoHolderImpl implements DaoHolder {

    private NamedList<DaoHolderItem> items;
    private DaoService daoService;
    private String daoManagerName;

    public DaoHolderImpl(DaoService daoService) {
        this.items = new DefaultNamedList<>();
        this.items.setNotFoundMessage("Не найден dao-метод {0} в хранилище {1}", this);
        this.daoService = daoService;
    }

    public Object invokeDao(String name, Object... args) throws Exception {
        DaoHolderItem d = items.get(name);
        String dmn = d.getDaoManagerName();
        if (UtString.empty(dmn)) {
            dmn = getDaoManagerName();
        }
        DaoManager dm = daoService.getDaoManager(dmn);
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
