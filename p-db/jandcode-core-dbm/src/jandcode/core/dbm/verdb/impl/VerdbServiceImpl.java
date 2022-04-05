package jandcode.core.dbm.verdb.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.verdb.*;

public class VerdbServiceImpl extends BaseModelMember implements VerdbService {

    private NamedList<VerdbModuleDef> verdbModules = new DefaultNamedList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf mConf = getModel().getConf();

        for (Conf x : mConf.getConfs("verdb-module")) {
            VerdbModuleDef md = new VerdbModuleDefImpl(getModel(), x);
            verdbModules.add(md);
        }
    }

    public NamedList<VerdbModuleDef> getVerdbModules() {
        return verdbModules;
    }

}
