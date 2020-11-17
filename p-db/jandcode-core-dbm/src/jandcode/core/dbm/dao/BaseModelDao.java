package jandcode.core.dbm.dao;

import jandcode.core.dao.*;
import jandcode.core.dbm.*;

/**
 * Базовый предок для dao, работаючих через модель
 */
public abstract class BaseModelDao extends BaseDao implements IModelMember {

    private Model model;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

}
