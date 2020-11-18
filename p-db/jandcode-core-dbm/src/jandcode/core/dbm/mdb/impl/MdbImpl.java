package jandcode.core.dbm.mdb.impl;

import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.db.std.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;

public class MdbImpl extends BaseDbWrapper implements Mdb {

    private Model model;
    private Db db;

    private MdbDaoImpl mdbDao;

    public MdbImpl(Model model, Db db) {
        this.model = model;
        this.db = db;
    }

    public App getApp() {
        return model.getApp();
    }

    protected Db getWrap() {
        return db;
    }

    public Model getModel() {
        return model;
    }

    ////// IMdbDao

    private MdbDaoImpl getMdbDao() {
        if (mdbDao == null) {
            mdbDao = new MdbDaoImpl(this);
        }
        return mdbDao;
    }

    public Object invokeDao(DaoMethodDef method, Object... args) throws Exception {
        return getMdbDao().invokeDao(method, args);
    }

    public <A> A createDao(Class<A> cls) {
        return getMdbDao().createDao(cls);
    }

    public DaoClassDef getDaoClassDef(Class cls) {
        return getMdbDao().getDaoClassDef(cls);
    }

    //////

}
