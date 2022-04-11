package jandcode.core.dbm.genid.impl;


import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.genid.*;

public class GenIdWrapper extends BaseModelMember implements GenId {

    protected GenIdImpl wrapper;

    public GenIdWrapper(GenId wrapper) {
        this.wrapper = (GenIdImpl) wrapper;
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
}
