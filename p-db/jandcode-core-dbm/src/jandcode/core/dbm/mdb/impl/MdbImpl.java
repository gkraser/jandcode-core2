package jandcode.core.dbm.mdb.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.db.std.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

public class MdbImpl extends BaseDbWrapper implements Mdb {

    private Model model;
    private Db db;

    private MdbDaoImpl mdbDao;
    private MdbDomainImpl mdbDomain;

    public MdbImpl(Model model, Db db) {
        this.model = model;
        this.db = db;
    }

    public App getApp() {
        return model.getApp();
    }

    protected Db getWrap() {
        return db;
    }

    public Model getModel() {
        return model;
    }

    ////// IMdbDao

    private MdbDaoImpl getMdbDao() {
        if (mdbDao == null) {
            mdbDao = new MdbDaoImpl(this);
        }
        return mdbDao;
    }

    public Object invokeDao(DaoMethodDef method, Object... args) throws Exception {
        return getMdbDao().invokeDao(method, args);
    }

    public DaoContext invokeDao(DaoContextIniter ctxIniter, DaoMethodDef method, Object... args) throws Exception {
        return getMdbDao().invokeDao(ctxIniter, method, args);
    }

    public <A> A createDao(Class<A> cls) {
        return getMdbDao().createDao(cls);
    }

    public DaoClassDef getDaoClassDef(Class cls) {
        return getMdbDao().getDaoClassDef(cls);
    }

    ////// IMdbDomain

    private MdbDomainImpl getMdbDomain() {
        if (mdbDomain == null) {
            mdbDomain = new MdbDomainImpl(this);
        }
        return mdbDomain;
    }

    public NamedList<Domain> getDomains() {
        return getMdbDomain().getDomains();
    }

    public Domain getDomain(String name) {
        return getMdbDomain().getDomain(name);
    }

    public Domain findDomain(String name) {
        return getMdbDomain().findDomain(name);
    }

    public DomainBuilder createDomainBuilder(String parentDomain) {
        return getMdbDomain().createDomainBuilder(parentDomain);
    }

    public Domain createDomain(Conf x, String name) {
        return getMdbDomain().createDomain(x, name);
    }

    public Store createStore(Domain domain) {
        return getMdbDomain().createStore(domain);
    }

    ////// IMdbMisc

    public Store createStore(String domainName) {
        return createStore(getDomain(domainName));
    }

}
