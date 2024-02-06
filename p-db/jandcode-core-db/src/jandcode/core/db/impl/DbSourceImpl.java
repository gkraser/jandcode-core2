package jandcode.core.db.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.db.*;

import java.util.*;

public class DbSourceImpl extends BaseComp implements DbSource, IBeanIniter {

    private DbDriver dbDriver;
    private Conf conf;

    private DbSourceProps props = new DbSourcePropsImpl();
    private BeanFactory beanFactory = new DefaultBeanFactory(this);

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

        // jdbcDriverClass прописываем для информации
        if (!UtString.empty(this.dbDriver.getJdbcDriverClassName())) {
            props.put("jdbcDriverClass", this.dbDriver.getJdbcDriverClassName());
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
        if (inst instanceof IDbConnect dbConn) {
            // db properties
            IVariantMap props = getProps().subMap("db");
            for (String key : props.keySet()) {
                String val = props.getString(key);
                if ("fetchSize".equals(key)) {
                    dbConn.getDbParams().setFetchSize(UtCnv.toInt(val));
                }
            }
        }
    }

    //////

    public DbDriver getDbDriver() {
        return dbDriver;
    }

    protected DefaultDb createDbInst() {
        return create(DefaultDb.class);
    }

    public Db createDb(boolean direct) {
        DefaultDb db = createDbInst();
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

    public DbSource createSystemDbSource() {
        DbSource systemDbSource = cloneComp();
        systemDbSource.getProps().putAll(getProps().subMap("system", true));
        return systemDbSource;
    }

    public void disconnectAll() {
        for (var connSvc : getBeanFactory().impl(DbConnectionService.class)) {
            connSvc.disconnectAll();
        }
    }

}
