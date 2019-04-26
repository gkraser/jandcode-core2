package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.db.*;

public class DbDriverServiceImpl extends BaseComp implements DbDriverService {

    private NamedList<DbDriverDef> dbDrivers = new DefaultNamedList<>("dbdriver [{0}] not found");

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
            DbDriverDef di = new DbDriverDefImpl(getApp(), x);
            dbDrivers.add(di);
        }

    }

    public NamedList<DbDriverDef> getDbDrivers() {
        return dbDrivers;
    }

}
