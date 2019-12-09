package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dao.*;

import java.util.*;

public class DaoServiceImpl extends BaseComp implements DaoService {

    private DaoClassDefFactory daoClassDefFactory = new DaoClassDefFactory();
    private NamedList<DaoManagerDef> daoManagers = new DefaultNamedList<>("DaoManager [{0}] not found");
    private NamedList<DaoHolderDef> daoHolders = new DefaultNamedList<>("DaoHolder [{0}] not found");

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

    class DaoHolderDef extends Named {
        Conf conf;
        DaoHolderImpl inst;

        DaoHolderDef(Conf conf) {
            setName(conf.getName());
            this.conf = conf;
        }

        DaoHolder getInst() {
            if (this.inst == null) {
                synchronized (this) {
                    if (this.inst == null) {
                        this.inst = getApp().create(this.conf, DaoHolderImpl.class);
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

        //
        for (Conf x : getApp().getConf().getConfs("dao/dao-holder")) {
            daoHolders.add(new DaoHolderDef(x));
        }

    }

    public DaoManager getDaoManager(String name) {
        return daoManagers.get(name).getInst();
    }

    public Collection<String> getDaoManagerNames() {
        return daoManagers.getNames();
    }

    public DaoHolder getDaoHolder(String name) {
        return daoHolders.get(name).getInst();
    }

    public Collection<String> getDaoHolderNames() {
        return daoHolders.getNames();
    }

    public DaoClassDef getDaoClassDef(Class cls) {
        return daoClassDefFactory.getDaoClassDef(cls);
    }

}
