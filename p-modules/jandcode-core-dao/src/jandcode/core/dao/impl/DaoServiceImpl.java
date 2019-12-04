package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dao.*;

import java.util.*;

public class DaoServiceImpl extends BaseComp implements DaoService {

    private NamedList<DaoManagerDef> daoManagers = new DefaultNamedList<>("DaoManager [{0}] not found");

    class DaoManagerDef extends Named {
        Conf conf;
        DaoManager inst;

        DaoManagerDef(Conf conf) {
            setName(conf.getName());
            this.conf = conf;
        }

        DaoManager getInst() {
            if (this.inst == null) {
                synchronized (this) {
                    if (this.inst == null) {
                        this.inst = getApp().create(this.conf, DaoManagerImpl.class);
                    }
                }
            }
            return this.inst;
        }

    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // формируем раскрытую conf для dao-manager
        Conf xExp = UtConf.create();
        xExp.setValue("dao-manager", getApp().getConf().getConf("dao/dao-manager"));

        ConfExpander exp = UtConf.createExpander(xExp);

        //
        Conf confDaoManager = exp.expand("dao-manager");
        for (Conf x : confDaoManager.getConfs()) {
            daoManagers.add(new DaoManagerDef(x));
        }

    }

    public DaoManager getDaoManager(String name) {
        return daoManagers.get(name).getInst();
    }

    public Collection<String> getDaoManagerNames() {
        return daoManagers.getNames();
    }

}
