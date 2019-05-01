package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.db.*;

import java.util.*;

public class DbDriverServiceImpl extends BaseComp implements DbDriverService {

    private NamedList<DbDriverDef> dbDrivers = new DefaultNamedList<>("dbdriver [{0}] not found");

    class DbDriverDef extends Named {
        Conf conf;
        private DbDriver inst;

        public DbDriverDef(Conf conf) {
            setName(conf.getName());
            this.conf = conf;
        }

        public DbDriver getInst() {
            if (this.inst == null) {
                synchronized (this) {
                    if (this.inst == null) {
                        this.inst = (DbDriver) getApp().create(this.conf);
                    }
                }
            }
            return this.inst;
        }

    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //

        // формируем раскрытую conf для dbdriver
        Conf xExp = UtConf.create();
        xExp.setValue("dbdriver", getApp().getConf().getConf("db/dbdriver"));

        ConfExpander exp = UtConf.createExpander(xExp);

        // drivers
        Conf confDbDrv = exp.expand("dbdriver");
        for (Conf x : confDbDrv.getConfs()) {
            DbDriverDef di = new DbDriverDef(x);
            dbDrivers.add(di);
        }

    }

    public DbDriver getDbDriver(String name) {
        return dbDrivers.get(name).getInst();
    }

    public Collection<String> getDbDriverNames() {
        return dbDrivers.getNames();
    }

}
