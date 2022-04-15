package jandcode.core.dbm.fixture;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.impl.*;
import jandcode.core.dbm.mdb.*;

/**
 * Предок построителей fixture
 */
public abstract class BaseFixtureBuilder extends BaseComp implements FixtureBuilder {

    private Fixture fx;
    private Db db;
    private Mdb mdb;

    /**
     * Реализация построения
     */
    protected abstract void onBuild();

    public Fixture build(Model model) {
        this.fx = Fixture.create(model);
        setApp(model.getApp());
        doInternalBuild();
        return this.fx;
    }

    /**
     * Какую фикстуру строим (синоним для getFixture())
     */
    protected Fixture getFx() {
        return fx;
    }

    /**
     * Какую фикстуру строим (синоним для getFx())
     */
    protected Fixture getFixture() {
        return fx;
    }

    /**
     * Для какой модели
     */
    protected Model getModel() {
        return getFx().getModel();
    }

    /**
     * mdb с установленным соединением
     */
    protected Mdb getMdb() {
        if (this.mdb == null) {
            Db dbTmp = getModel().createDb();
            this.db = new ModelDbWrapper(dbTmp, true, false);
            this.mdb = getModel().createMdb(this.db);
        }
        return this.mdb;
    }

    //////

    private void doInternalBuild() {
        try {
            onBuild();
        } finally {
            if (this.db != null) {
                if (this.db.isConnected()) {
                    // кто то попользовался
                    if (this.db.isTran()) {
                        try {
                            this.db.rollback();
                        } catch (Exception e) {
                            throw new XErrorWrap(e);
                        }
                    }
                    try {
                        this.db.disconnectForce();
                    } catch (Exception e) {
                        throw new XErrorWrap(e);
                    }
                }
            }
            this.db = null;
            this.mdb = null;
        }
    }

}
