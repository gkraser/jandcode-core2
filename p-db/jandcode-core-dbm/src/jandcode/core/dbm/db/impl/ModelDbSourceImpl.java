package jandcode.core.dbm.db.impl;

import jandcode.core.db.impl.*;
import jandcode.core.dbm.*;

/**
 * dbsource с привязкой к модели
 */
public class ModelDbSourceImpl extends DbSourceImpl implements IModelMember {

    private Model model;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

}
