package jandcode.db.impl;

import jandcode.core.*;
import jandcode.db.*;
import jandcode.commons.conf.*;

public class DbDriverDefImpl extends BaseComp implements DbDriverDef {

    private Conf conf;

    public DbDriverDefImpl(App app, Conf conf, String name) {
        setApp(app);
        setName(name);
        this.conf = conf;
    }

    public DbDriver createInst(DbSource dbSource) {
        return (DbDriver) dbSource.create(conf);
    }

    public Conf getConf() {
        return conf;
    }

    public String getDbType() {
        return this.conf.getString("dbtype");
    }

}
