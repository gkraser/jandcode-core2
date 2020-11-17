package jandcode.core.dbm.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import org.slf4j.*;

public class ModelDbServiceImpl extends BaseModelMember implements ModelDbService {

    protected static Logger log = LoggerFactory.getLogger(ModelDbService.class);

    private ModelDbSourceImpl dbSource;

    //////

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        DbService appDbService = getApp().bean(DbService.class);

        Conf dbsConf = getModel().getConf().getConf("dbsource/default");
        dbSource = (ModelDbSourceImpl) appDbService.createDbSource(dbsConf);
        dbSource.setModel(getModel());
    }

    public DbSource getDbSource() {
        return dbSource;
    }

}
