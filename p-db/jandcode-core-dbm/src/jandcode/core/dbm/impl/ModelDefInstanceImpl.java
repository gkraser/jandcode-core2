package jandcode.core.dbm.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

import java.util.*;

/**
 * Описание экземпляра модели, полученной из conf
 */
public class ModelDefInstanceImpl extends ModelDefImpl {

    private ModelDefImpl modelInstanceOf;

    public ModelDefInstanceImpl(App app, String name, Conf conf, ModelDefImpl modelInstanceOf) {
        super(app, name, conf);
        this.modelInstanceOf = modelInstanceOf;
    }

    protected List<ModelDef> createIncludedModels() {
        // instance = include(instance)!
        List<ModelDef> res = ((ModelDefImpl) getInstanceOf()).createIncludedModels();
        res.add(getInstanceOf());
        return res;
    }

    public boolean isInstance() {
        return true;
    }

    public ModelDef getInstanceOf() {
        return modelInstanceOf;
    }

}
