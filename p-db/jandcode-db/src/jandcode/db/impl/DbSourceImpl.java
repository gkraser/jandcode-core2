package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.db.*;

import java.util.*;

public class DbSourceImpl extends BaseComp implements DbSource, IBeanIniter {

    private DbDriver dbDriver;
    private Conf conf;

    private DbSourceProps props = new DbSourcePropsImpl();

    private ThreadLocalDb threadLocalDb = new ThreadLocalDb();
    private BeanFactory beanFactory = new DefaultBeanFactory(this);

    protected class ThreadLocalDb extends ThreadLocal<Db> {
        protected Db initialValue() {
            return createDb();
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        DbSourceConfBuilder cb = new DbSourceConfBuilder();
        this.conf = cb.buildConf(getApp(), cfg.getConf());
        this.dbDriver = getApp().bean(DbDriverService.class).getDbDriver(this.conf.getString("dbdriver"));
        //
        for (Map.Entry<String, Object> a : this.conf.entrySet()) {
            String key = a.getKey();
            Object value = a.getValue();
            if (value instanceof Conf) {
                continue;
            }
            if (key.startsWith("^")) {
                continue;  // внутренние штучки
            }
            props.put(key, UtCnv.toString(value));
        }

        //
        getBeanFactory().beanConfigure(new DefaultBeanConfig(this.conf));
    }

    public Conf getConf() {
        return conf;
    }

    //////

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IDbSourceMember) {
            ((IDbSourceMember) inst).setDbSource(this);
        }
    }

    //////

    public DbDriver getDbDriver() {
        return dbDriver;
    }

    public Db createDb(boolean direct) {
        DefaultDb db = create(DefaultDb.class);
        if (direct) {
            db.setConnectionService((DbConnectionService) bean(DbConsts.BEAN_DIRECT_CONNECT));
        } else {
            db.setConnectionService(bean(DbConnectionService.class));
        }
        return db;
    }

    public Db createDb() {
        return createDb(false);
    }

    public Db getDb() {
        return threadLocalDb.get();
    }

    public String getDbType() {
        return getDbDriver().getDbType();
    }

    //////

    public DbSourceProps getProps() {
        return props;
    }

    //////

    public DbSource cloneComp() {
        DbSourceImpl dbs = getApp().create(this.conf, DbSourceImpl.class);
        // накладываем свойства текущие
        for (String key : props.keySet()) {
            dbs.props.put(key, props.getRaw(key));
        }
        //
        return dbs;
    }

}
