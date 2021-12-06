package jandcode.core.dbm.mdb.impl;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.db.std.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.sql.*;
import jandcode.core.store.*;

import java.util.*;

public class MdbImpl extends BaseDbWrapper implements Mdb {

    private Model model;
    private Db db;
    private DaoInvoker daoInvoker;
    private DomainService domainService;
    private DictService dictService;
    private StoreService storeService;
    private SqlService sqlService;

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

    private IDaoInvoker getIDaoInvoker() {
        if (daoInvoker == null) {
            daoInvoker = getModel().bean(ModelDaoService.class).getDaoInvoker();
        }
        return daoInvoker;
    }

    public Object invokeDao(DaoMethodDef method, Object... args) throws Exception {
        return getIDaoInvoker().invokeDao(method, args);
    }

    public DaoContext invokeDao(DaoContextIniter ctxIniter, DaoMethodDef method, Object... args) throws Exception {
        return getIDaoInvoker().invokeDao(ctxIniter, method, args);
    }

    public <A> A createDao(Class<A> cls) {
        return getIDaoInvoker().createDao(cls);
    }

    public DaoClassDef getDaoClassDef(Class cls) {
        return getIDaoInvoker().getDaoClassDef(cls);
    }

    ////// IMdbDomain

    private DomainService getDomainService() {
        if (domainService == null) {
            domainService = getModel().bean(DomainService.class);
        }
        return domainService;
    }

    public NamedList<Domain> getDomains() {
        return getDomainService().getDomains();
    }

    public DomainBuilder createDomainBuilder(String parentDomain) {
        return getDomainService().createDomainBuilder(parentDomain);
    }

    public Domain createDomain(Conf x, String name) {
        return getDomainService().createDomain(x, name);
    }

    public Store createStore(Domain domain) {
        return getDomainService().createStore(domain);
    }

    ////// IDictService

    public DictService getDictService() {
        if (dictService == null) {
            dictService = getModel().bean(DictService.class);
        }
        return dictService;
    }

    public NamedList<Dict> getDicts() {
        return getDictService().getDicts();
    }

    public void resolveDicts(Object data) throws Exception {
        getDictService().resolveDicts(data);
    }

    public DictData loadDictData(Dict dict) throws Exception {
        return getDictService().loadDictData(dict);
    }

    public DictData loadDictData(Dict dict, Collection<Object> ids) throws Exception {
        return getDictService().loadDictData(dict, ids);
    }

    ////// ILoadQueryRecord

    protected StoreRecord oneRecord(Store st) {
        if (st.size() == 0) {
            throw new XError("Нет записей");
        }
        if (st.size() > 1) {
            throw new XError("Слишком много записей ({0))", st.size());
        }
        return st.get(0);
    }

    public StoreRecord loadQueryRecord(CharSequence sql) throws Exception {
        Store st = loadQuery(sql);
        return oneRecord(st);
    }

    public StoreRecord loadQueryRecord(CharSequence sql, Object params) throws Exception {
        Store st = loadQuery(sql, params);
        return oneRecord(st);
    }

    ////// IStoreService

    private StoreService getStoreService() {
        if (storeService == null) {
            storeService = getApp().bean(StoreService.class);
        }
        return storeService;
    }

    public StoreField createStoreField(String type) {
        return getStoreService().createStoreField(type);
    }

    public StoreField createStoreField(Class valueType) {
        return getStoreService().createStoreField(valueType);
    }

    public Store createStore(Class cls) {
        return getStoreService().createStore(cls);
    }

    ////// ISqlService

    private SqlService getSqlService() {
        if (sqlService == null) {
            sqlService = getModel().bean(SqlService.class);
        }
        return sqlService;
    }

    public SqlText createSqlText(String sql) {
        return getSqlService().createSqlText(sql);
    }

}
