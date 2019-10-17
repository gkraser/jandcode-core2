package jandcode.core.db.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.db.*;

public class DbServiceImpl extends BaseComp implements DbService {

    private NamedList<DbSourceDef> dbSources = new DefaultNamedList<>("Не найдена конфигурация cfg/dbsource/{0}");

    class DbSourceDef extends Named {
        Conf conf;
        private DbSource inst;

        public DbSourceDef(Conf conf) {
            setName(conf.getName());
            this.conf = conf;
        }

        DbSource getInst() {
            if (this.inst == null) {
                synchronized (this) {
                    if (this.inst == null) {
                        this.inst = createInst();
                    }
                }
            }
            return this.inst;
        }

        DbSource createInst() {
            return createDbSource(this.conf);
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        for (Conf conf1 : getApp().getConf().getConfs("cfg/dbsource")) {
            DbSourceDef dd = new DbSourceDef(conf1);
            dbSources.add(dd);
        }
    }

    //////

    public DbSource createDbSource(Conf conf) {
        return getApp().create(conf, DbSourceImpl.class);
    }

    public DbSource createDbSource(String name) {
        return dbSources.get(name).createInst();
    }

    public DbSource getDbSource(String name) {
        return dbSources.get(name).getInst();
    }

}
