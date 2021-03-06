package jandcode.core.dbm.dao;

import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;

/**
 * Базовый предок для dao, работающих через модель
 */
public abstract class BaseModelDao extends BaseDao implements IModelMember {

    private Model model;
    private Mdb mdb;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * База данных для работы.
     */
    protected Db getDb() {
        return getContext().bean(Db.class);
    }

    /**
     * База данных и утилиты модули для работы.
     */
    protected Mdb getMdb() {
        if (mdb == null) {
            mdb = getModel().createMdb(getDb());
        }
        return mdb;
    }

}
