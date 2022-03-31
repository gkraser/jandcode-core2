package jandcode.core.dbm.verdb.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.verdb.*;

public class VerdbServiceImpl extends BaseModelMember implements VerdbService {

    private NamedList<VerdbModule> verdbModules = new DefaultNamedList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf mConf = getModel().getConf();

        for (Conf x : mConf.getConfs("verdb-module")) {
            VerdbModule m = getModel().create(x, VerdbModuleImpl.class);

            if (UtString.empty(m.getPath()) || !UtFile.existsFileObject(m.getPath())) {
                throw new XError("Для verdb-module [{0}] указан не существующий путь [{1}]: {2}",
                        m.getName(), m.getPath(), x.origin());
            }

            verdbModules.add(m);
        }
    }

    public NamedList<VerdbModule> getVerdbModules() {
        return verdbModules;
    }

}
