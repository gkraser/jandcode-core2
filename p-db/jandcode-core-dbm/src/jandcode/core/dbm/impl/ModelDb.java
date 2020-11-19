package jandcode.core.dbm.impl;

import jandcode.core.db.impl.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.store.*;
import jandcode.core.store.*;

/**
 * Перекрытая db с адаптациией к модели
 */
public class ModelDb extends DefaultDb {

    public Model getModel() {
        return ((IModelLink) getDbSource()).getModel();
    }

    //////

    public Store createStore() {
        // store создаем модельный сервис
        ModelStoreService svc = getModel().bean(ModelStoreService.class);
        return svc.createStore();
    }

}
