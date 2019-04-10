package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.db.*;
import jandcode.commons.named.*;
import jandcode.commons.conf.*;

public class DbServiceImpl extends BaseComp implements DbService {

    private NamedList<DbSourceDef> dbSources = new DefaultNamedList<>();
    private NamedList<DbDriverDef> dbDrivers = new DefaultNamedList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // формируем раскрытую conf для тега db
        Conf xExp = UtConf.create();
        xExp.join(getApp().getConf().getConf("db"));

        ConfExpander exp = UtConf.createExpander(xExp);

        // drivers
        for (Conf rtx : exp.expand("dbdriver").getConfs()) {
            DbDriverDef di = new DbDriverDefImpl(getApp(), rtx, rtx.getName());
            dbDrivers.add(di);
        }

        // dbsources
        for (Conf rtx : exp.expand("dbsource").getConfs()) {
            DbSourceDef di = new DbSourceDefImpl(getApp(), rtx.getName(), rtx);
            dbSources.add(di);
        }

    }

    //////

    public NamedList<DbSourceDef> getDbSources() {
        return dbSources;
    }

    public NamedList<DbDriverDef> getDbDrivers() {
        return dbDrivers;
    }

    public DbDriverDef resolveDbDriver(Conf x) {
        DbSourceDefImpl dbsInfo = new DbSourceDefImpl(getApp(), x.getName(), x);
        Conf xj = dbsInfo.getJoinConf();
        return getDbDrivers().get(xj.getString("dbdriver", DbConsts.DBDRIVER_DEFAULT));
    }

    //////

    public DbSource createDbSource(String dbsourceName) {
        return dbSources.get(dbsourceName).createInst();
    }

    public DbSource createDbSource(Conf conf) {
        Conf tmp = UtConf.create(conf.getName());
        tmp.join(conf);
        DbSourceDef dbsInfo = new DbSourceDefImpl(getApp(), conf.getName(), tmp);
        return dbsInfo.createInst();
    }

    public DbSource getDbSource(String dbsourceName) {
        return dbSources.get(dbsourceName).getInst();
    }

}
