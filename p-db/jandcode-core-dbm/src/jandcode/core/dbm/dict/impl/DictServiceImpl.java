package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dict.*;

public class DictServiceImpl extends BaseModelMember implements DictService {

    private NamedList<Dict> dicts = new DefaultNamedList<>("Не найден dict [{0}]");

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //

        Conf modelConf = getModel().getConf();
        Conf xExp = UtConf.create();
        xExp.setValue("dict", modelConf.getConf("dict"));

        ConfExpander exp = UtConf.createExpander(xExp);
        exp.addRuleNotInherited("abstract");

        Conf confDict = exp.expand("dict");
        for (Conf x : confDict.getConfs()) {
            if (UtConf.isTagged(x, "abstract")) {
                continue;
            }
            Dict d = getModel().create(x, DictImpl.class);
            dicts.add(d);
        }

    }

    public NamedList<Dict> getDicts() {
        return dicts;
    }

}
