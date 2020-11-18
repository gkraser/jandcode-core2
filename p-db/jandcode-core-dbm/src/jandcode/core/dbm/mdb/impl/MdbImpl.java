package jandcode.core.dbm.mdb.impl;

import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.db.std.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;

public class MdbImpl extends BaseDbWrapper implements Mdb {

    private Model model;
    private Db db;

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

}
