package jandcode.core.dbm.dao.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;

import java.util.*;

public class ModelDaoServiceImpl extends BaseModelMember implements ModelDaoService {

    private DaoInvoker daoInvoker;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf modelCof = getModel().getConf();
        this.daoInvoker = (DaoInvoker) getModel().create(modelCof.getConf("dao/daoInvoker/default"));
    }

    public DaoInvoker getDaoInvoker() {
        return daoInvoker;
    }

    public <A> A createDao(Class<A> cls) {
        return getDaoInvoker().createDao(cls);
    }

    public Object invokeDao(DaoMethodDef method, Object... args) throws Exception {
        return getDaoInvoker().invokeDao(method, args);
    }

    public Collection<DaoFilter> getDaoFilters() {
        return getDaoInvoker().getDaoFilters();
    }

    public DaoClassDef getDaoClassDef(Class cls) {
        return getDaoInvoker().getDaoClassDef(cls);
    }

}
