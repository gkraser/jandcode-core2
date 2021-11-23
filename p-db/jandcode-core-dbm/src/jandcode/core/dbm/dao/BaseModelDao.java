package jandcode.core.dbm.dao;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;

/**
 * Базовый предок для dao, работающих через модель
 */
public abstract class BaseModelDao extends BaseDao {

    private Model model;
    private Mdb mdb;

    /**
     * Модель, в контексте которой выполняется dao.
     */
    protected Model getModel() {
        if (model == null) {
            BeanDef b = getContext().getBeanFactory().findBean(Model.class, false);
            if (b == null) {
                throw new XError("В контексте dao отсутствует {0}, возможно dao " +
                        "выполняется в invoker, который не умеет работать с Model/Db",
                        Model.class.getName());
            }
            model = getContext().bean(Model.class);
        }
        return model;
    }

    /**
     * База данных и утилиты модули для работы.
     */
    protected Mdb getMdb() {
        if (mdb == null) {
            mdb = getModel().createMdb(getContext().bean(Db.class));
        }
        return mdb;
    }

}
