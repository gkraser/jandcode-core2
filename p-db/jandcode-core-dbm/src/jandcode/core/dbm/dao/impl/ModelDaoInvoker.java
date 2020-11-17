package jandcode.core.dbm.dao.impl;

import jandcode.core.dao.*;
import jandcode.core.dbm.*;

public class ModelDaoInvoker extends DefaultDaoInvoker implements IModelMember {

    private Model model;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

}
