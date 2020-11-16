package jandcode.core.dbm.db.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.db.*;
import org.slf4j.*;

public class ModelDbServiceImpl extends BaseModelMember implements ModelDbService {

    protected static Logger log = LoggerFactory.getLogger(ModelDbService.class);

    private ModelDbSourceImpl dbSource;
    private ThreadLocalDb threadLocalDb = new ThreadLocalDb();

    protected class ThreadLocalDb extends ThreadLocal<Db> {
        protected Db initialValue() {
            return createDb();
        }
    }

    //////

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        DbService appDbService = getApp().bean(DbService.class);

        Conf dbsRt = getModel().getConf().getConf("dbsource/default");
        dbSource = (ModelDbSourceImpl) appDbService.createDbSource(dbsRt);
        dbSource.setModel(getModel());
    }

    public DbSource getDbSource() {
        return dbSource;
    }

    public Db getDb() {
        log.warn("Use getDb() in ModelDbService");
        return threadLocalDb.get();
    }

}
