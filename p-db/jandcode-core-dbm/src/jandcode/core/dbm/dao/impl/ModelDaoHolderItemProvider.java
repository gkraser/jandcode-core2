package jandcode.core.dbm.dao.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.dao.impl.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;

import java.util.*;

public class ModelDaoHolderItemProvider extends BaseComp implements DaoHolderItemProvider {

    public Collection<DaoHolderItem> loadItems(Conf conf, String namePrefix) {
        Collection<DaoHolderItem> res = new ArrayList<>();

        String modelName = conf.getString("model", "default");
        Model model = getApp().bean(ModelService.class).getModel(modelName);

        ModelDaoService svc = model.bean(ModelDaoService.class);
        NamedList<DaoHolderItem> items = svc.getDaoHolder().getItems();

        String invokerName = "model:" + model.getName();
        for (DaoHolderItem it : items) {
            res.add(new DaoHolderItemImpl(namePrefix + it.getName(), it.getMethodDef(), invokerName));
        }

        return res;
    }

}
