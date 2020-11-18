package jandcode.core.dbm.dao.impl;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;

/**
 * Для имени daoInvoker вида 'model:MODELNAME'.
 */
public class ModelDaoInvokerResolver extends BaseComp implements DaoInvokerResolver {

    public DaoInvoker resolveDaoInvoker(String name) {
        String modelName = UtString.removePrefix(name, "model:");
        if (!UtString.empty(modelName)) {
            ModelDef modelDef = getApp().bean(ModelService.class).getModels().get(modelName);
            Model model = modelDef.getInst();
            ModelDaoService svc = model.bean(ModelDaoService.class);
            return svc.getDaoInvoker();
        }
        return null;
    }

}
