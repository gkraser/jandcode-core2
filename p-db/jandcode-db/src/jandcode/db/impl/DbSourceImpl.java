package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.db.*;

import java.util.*;

public class DbSourceImpl extends BaseComp implements DbSource, IBeanIniter {

    private DbDriver dbDriver;
    private Conf conf;

    private IVariantMap props;
    private IVariantMap propsRaw = new VariantMap();
    private PropsExpander propsExp = new PropsExpander(propsRaw);

    private ThreadLocalDb threadLocalDb = new ThreadLocalDb();
    private BeanFactory beanFactory = new DefaultBeanFactory(this);

    private List<String> initConnectionSqls;

    protected class ThreadLocalDb extends ThreadLocal<Db> {
        protected Db initialValue() {
            return createDb();
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        //
        this.conf = cfg.getConf();
        for (Map.Entry<String, Object> a : this.conf.entrySet()) {
            String key = a.getKey();
            Object value = a.getValue();
            if (value instanceof Conf) {
                continue;
            }
            propsRaw.put(key, UtCnv.toString(value));
        }

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

    protected void setDbDriver(DbDriver dbDriver) {
        this.dbDriver = dbDriver;
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

    //////

    public void setProp(String name, Object value) {
        props = null;
        propsRaw.put(name, value);
    }

    public IVariantMap getProps() {
        if (props == null) {
            synchronized (this) {
                if (props == null) {
                    VariantMap vm = new VariantMap();
                    propsExp.expandAllTo(vm);
                    props = vm;
                }
            }
        }
        return props;
    }

    public IVariantMap getProps(String prefix, boolean override) {
        IVariantMap prp = getProps();
        prefix = prefix + ".";
        VariantMap res = new VariantMap();
        if (override) {
            // сначала без префикса
            for (String key : prp.keySet()) {
                if (!key.startsWith(prefix)) {
                    res.put(key, prp.get(key));
                }
            }
        }
        // накладываем с префиксом
        for (String key : prp.keySet()) {
            String k2 = UtString.removePrefix(key, prefix);
            if (k2 != null) {
                res.put(k2, prp.get(key));
            }
        }
        return res;
    }

    //////

    public DbSource cloneComp() {
        return getApp().create(this.conf, DbSourceImpl.class, (inst) -> {
            ((DbSourceImpl) inst).setDbDriver(dbDriver);
        });
    }

    //////

    public List<String> getInitConnectionSqls() {
        if (initConnectionSqls == null) {
            synchronized (this) {
                if (initConnectionSqls == null) {
                    initConnectionSqls = grabInitConnectionSqls();
                }
            }
        }
        return initConnectionSqls;
    }

    protected List<String> grabInitConnectionSqls() {
        List<String> res = new ArrayList<>();
        IVariantMap p = getProps(DbSourcePropsConsts.initConnectionSql, false);
        TreeMap<String, Object> m = new TreeMap<>(p);
        for (Object s : m.values()) {
            res.add(UtCnv.toString(s));
        }
        return res;
    }

}
