package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dao.*;

import java.util.*;

public class DaoServiceImpl extends BaseComp implements DaoService {

    private DaoClassDefFactory daoClassDefFactory = new DaoClassDefFactory();
    private NamedList<DaoInvokerDef> daoInvokers = new DefaultNamedList<>("DaoInvoker [{0}] not found");
    private NamedList<DaoHolderDef> daoHolders = new DefaultNamedList<>("DaoHolder [{0}] not found");

    class DaoInvokerDef extends Named {
        Conf conf;
        DaoInvoker inst;

        DaoInvokerDef(Conf conf) {
            setName(conf.getName());
            this.conf = conf;
        }

        DaoInvoker getInst() {
            if (this.inst == null) {
                synchronized (this) {
                    if (this.inst == null) {
                        this.inst = getApp().create(this.conf, DaoInvokerImpl.class);
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

        // формируем раскрытую conf для dao-invoker
        Conf xExp = UtConf.create();
        xExp.setValue("dao-invoker", getApp().getConf().getConf("dao/dao-invoker"));

        ConfExpander exp = UtConf.createExpander(xExp);

        //
        Conf confDaoInvoker = exp.expand("dao-invoker");
        for (Conf x : confDaoInvoker.getConfs()) {
            daoInvokers.add(new DaoInvokerDef(x));
        }

        //
        for (Conf x : getApp().getConf().getConfs("dao/dao-holder")) {
            daoHolders.add(new DaoHolderDef(x));
        }

    }

    public DaoInvoker getDaoInvoker(String name) {
        return daoInvokers.get(name).getInst();
    }

    public Collection<String> getDaoInvokerNames() {
        return daoInvokers.getNames();
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
