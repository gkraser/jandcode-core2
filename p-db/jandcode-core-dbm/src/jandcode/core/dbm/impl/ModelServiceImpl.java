package jandcode.core.dbm.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

public class ModelServiceImpl extends BaseComp implements ModelService {

    private NamedList<ModelDef> models = new DefaultNamedList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // сначала модели
        for (Conf x : getApp().getConf().getConfs("dbm/model")) {
            String inst = x.getString("instance");
            if (!UtString.empty(inst)) {
                continue; // это экземпляр
            }
            ModelDefImpl md = new ModelDefImpl(getApp(), x.getName(), x);
            models.add(md);
        }

        // потом экземпляры моделей
        for (Conf x : getApp().getConf().getConfs("dbm/model")) {
            String inst = x.getString("instance");
            if (UtString.empty(inst)) {
                continue; // это модель
            }
            ModelDef m = models.find(inst);
            if (m == null) {
                throw new XError("Для экземпляра модели [{0}] указана не существующая модель [{1}]", x.getName(), inst);
            }
            Conf x1 = x.findConf("include");
            if (x1 != null) {
                throw new XError("Для экземпляра модели нельзя указывать теги include: [{0}]", x.getName());
            }
            ModelDefInstanceImpl md = new ModelDefInstanceImpl(getApp(), x.getName(), x, (ModelDefImpl) m);
            models.add(md);
        }

    }

    public NamedList<ModelDef> getModels() {
        return models;
    }

    public Model getModel(String name) {
        return models.get(name).getInst();
    }

    public Model getModel() {
        return getModel(DbmConsts.DEFAULT);
    }

}
