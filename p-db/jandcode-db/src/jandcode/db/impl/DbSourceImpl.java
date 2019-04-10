package jandcode.db.impl;

import jandcode.core.*;
import jandcode.db.*;
import jandcode.commons.*;
import jandcode.commons.conf.*;

import java.util.*;

public class DbSourceImpl extends BaseComp implements DbSource, ISubstVar, IBeanIniter {

    private MapSubst props = new MapSubst();
    private DbDriver dbDriver;
    private ThreadLocalDb threadLocalDb = new ThreadLocalDb();
    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    private DbSourceDef dbSourceDef;
    private Conf conf;

    protected class ThreadLocalDb extends ThreadLocal<Db> {
        protected Db initialValue() {
            return createDb();
        }
    }

    // map с подстановками
    protected class MapSubst extends HashMap<String, String> implements ISubstVar {

        public String get(Object key) {
            String v = super.get(key);
            return UtString.substVar(v, this);
        }

        public String getRaw(Object key) {
            return super.get(key);
        }

        public String onSubstVar(String v) {
            String res = getRaw(v);
            if (res == null) {
                res = "";
            }
            return res;
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        //
        this.conf = cfg.getConf();
        for (Map.Entry<String, Object> a : this.conf.entrySet()) {
            getProps().put(a.getKey(), UtCnv.toString(a.getValue()));
        }
        //
        DbDriverDef drvdef = getApp().bean(DbService.class).getDbDrivers()
                .get(getConf().getString("dbdriver", DbConsts.DBDRIVER_DEFAULT));
        dbDriver = drvdef.createInst(this);

        //
        getBeanFactory().beanConfigure(cfg);
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

    public Db createDb() {
        return create(DbImpl.class);
    }

    public Db getDb() {
        return threadLocalDb.get();
    }

    public String getDbType() {
        return getDbDriver().getDbType();
    }

    /**
     * Установка создателя
     */
    public void setDbSourceDef(DbSourceDef dbSourceDef) {
        this.dbSourceDef = dbSourceDef;
    }

    //////

    public Map<String, String> getProps() {
        return props;
    }

    public Map<String, String> getProps(String prefix, boolean override) {
        prefix = prefix + ".";
        MapSubst res = new MapSubst();
        if (override) {
            // сначала без префикса
            for (String key : props.keySet()) {
                if (!key.startsWith(prefix)) {
                    res.put(key, props.getRaw(key));
                }
            }
        }
        // накладываем с префиксом
        for (String key : props.keySet()) {
            String k2 = UtString.removePrefix(key, prefix);
            if (k2 != null) {
                res.put(k2, props.getRaw(key));
            }
        }
        return res;
    }

    public String onSubstVar(String v) {
        String res = getProps().get(v);
        if (res == null) {
            res = "";
        }
        return res;
    }

    //////

    public DbSource cloneComp() {
        return dbSourceDef.createInst();
    }

}