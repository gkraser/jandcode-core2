package jandcode.core.dbm.mdb;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Базовый класс для утилит, привязанных к {@link Mdb}
 */
public abstract class BaseMdbUtils implements IAppLink, IModelLink, IMdbLink, IMdbLinkSet {

    private Mdb mdb;

    public BaseMdbUtils() {
    }

    public BaseMdbUtils(Mdb mdb) {
        this.mdb = mdb;
    }

    public Mdb getMdb() {
        if (mdb == null) {
            throw new XError("Mdb не назначена");
        }
        return mdb;
    }

    public void setMdb(Mdb mdb) {
        this.mdb = mdb;
    }

    public App getApp() {
        return getMdb().getApp();
    }

    public Model getModel() {
        return getMdb().getModel();
    }

}
