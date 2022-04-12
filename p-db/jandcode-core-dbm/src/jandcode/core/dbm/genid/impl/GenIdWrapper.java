package jandcode.core.dbm.genid.impl;


import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.mdb.*;

public class GenIdWrapper extends BaseModelMember implements GenId {

    protected GenId wrapper;
    private Mdb mdb;

    public GenIdWrapper(GenId wrapper, Mdb mdb) {
        this.wrapper = wrapper;
        this.mdb = mdb;
    }

    public String getName() {
        return wrapper.getName();
    }

    public App getApp() {
        return wrapper.getApp();
    }

    public Model getModel() {
        return wrapper.getModel();
    }

    //////

    public GenIdDriver getDriver() {
        return wrapper.getDriver();
    }

    public long getNextId() {
        return wrapper.getNextId();
    }

    public long getCurrentId() {
        return wrapper.getCurrentId();
    }

    public long getStart() {
        return wrapper.getStart();
    }

    public long getStep() {
        return wrapper.getStep();
    }

    //////

    public GenId withMdb(Mdb mdb) {
        return new GenIdWrapper(this, mdb);
    }

    public Mdb getMdb() {
        if (this.mdb == null) {
            return getModel().createMdb();
        } else {
            return this.mdb;
        }
    }

}
