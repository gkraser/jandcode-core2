package jandcode.core.dbm.impl;

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

    protected DefaultDb createDbInst() {
        return create(ModelDb.class);
    }

}
