package jandcode.core.dbm.impl;

import jandcode.core.db.impl.*;
import jandcode.core.dbm.*;

/**
 * Перекрытая db с адаптациией к модели
 */
public class ModelDb extends DefaultDb {

    public Model getModel() {
        return ((IModelLink) getDbSource()).getModel();
    }

}
