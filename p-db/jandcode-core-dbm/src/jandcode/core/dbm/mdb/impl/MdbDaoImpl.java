package jandcode.core.dbm.mdb.impl;

import jandcode.core.dao.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.mdb.*;

public class MdbDaoImpl implements IMdbDao {

    private Mdb mdb;
    private DaoInvoker daoInvoker;

    public MdbDaoImpl(Mdb mdb) {
        this.mdb = mdb;
    }

    private IDaoInvoker getIDaoInvoker() {
        if (daoInvoker == null) {
            daoInvoker = mdb.getModel().bean(ModelDaoService.class).getDaoInvoker();
        }
        return daoInvoker;
    }

    //////

    public Object invokeDao(DaoMethodDef method, Object... args) throws Exception {
        return getIDaoInvoker().invokeDao(method, args);
    }

    public <A> A createDao(Class<A> cls) {
        return getIDaoInvoker().createDao(cls);
    }

    public DaoClassDef getDaoClassDef(Class cls) {
        return getIDaoInvoker().getDaoClassDef(cls);
    }

}
