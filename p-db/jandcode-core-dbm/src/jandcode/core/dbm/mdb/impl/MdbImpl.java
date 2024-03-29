package jandcode.core.dbm.mdb.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.test.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.db.std.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.sql.*;
import jandcode.core.dbm.validate.*;
import jandcode.core.store.*;

import java.util.*;

public class MdbImpl extends BaseDbWrapper implements Mdb, IValidateErrorsLinkSet {

    private static final String FIELD_ID = "id";

    private Model model;
    private Db db;
    private DaoInvoker daoInvoker;
    private DomainService domainService;
    private DictService dictService;
    private StoreService storeService;
    private SqlService sqlService;
    private GenIdService genIdService;
    private ValidateErrors validateErrors;
    private ValidatorService validatorService;

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

    ////// Mdb

    public <A> A create(Class<A> cls) {
        A inst = getModel().create(cls);
        if (inst instanceof IMdbLinkSet) {
            ((IMdbLinkSet) inst).setMdb(this);
        }
        return (A) inst;
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

    public Domain createDomain(Store store) {
        return getDomainService().createDomain(store);
    }

    public Store createStore(Domain domain) {
        return getDomainService().createStore(domain);
    }

    public StoreRecord createStoreRecord(Domain domain) {
        return getDomainService().createStoreRecord(domain);
    }

    public StoreRecord createStoreRecord(Domain domain, StoreRecord data) {
        return getDomainService().createStoreRecord(domain, data);
    }

    public StoreRecord createStoreRecord(Domain domain, Map data) {
        return getDomainService().createStoreRecord(domain, data);
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

    public Object getDictValue(String dictName, Object id, String fieldName) {
        return getDictService().getDictValue(dictName, id, fieldName);
    }

    public Object getDictValue(String dictName, Object id) {
        return getDictService().getDictValue(dictName, id);
    }

    public String getDictText(String dictName, Object id, String fieldName) {
        return getDictService().getDictText(dictName, id, fieldName);
    }

    public String getDictText(String dictName, Object id) {
        return getDictService().getDictText(dictName, id);
    }

    ////// ILoadQueryRecord

    protected StoreRecord oneRecord(Store st, boolean required) {
        if (st.size() == 0) {
            if (!required) {
                return null;
            }
            throw new XError("Нет записей");
        }
        if (st.size() > 1) {
            throw new XError("Слишком много записей ({0})", st.size());
        }
        return st.get(0);
    }

    public StoreRecord loadQueryRecord(CharSequence sql) throws Exception {
        Store st = loadQuery(sql);
        return oneRecord(st, true);
    }

    public StoreRecord loadQueryRecord(CharSequence sql, boolean required) throws Exception {
        Store st = loadQuery(sql);
        return oneRecord(st, required);
    }

    public StoreRecord loadQueryRecord(CharSequence sql, Object params) throws Exception {
        Store st = loadQuery(sql, params);
        return oneRecord(st, true);
    }

    public StoreRecord loadQueryRecord(CharSequence sql, Object params, boolean required) throws Exception {
        Store st = loadQuery(sql, params);
        return oneRecord(st, required);
    }

    public StoreRecord loadQueryRecord(StoreRecord rec, CharSequence sql) throws Exception {
        rec.clear();
        StoreRecord tmp = loadQueryRecord(sql);
        rec.setValues(tmp);
        return rec;
    }

    public StoreRecord loadQueryRecord(StoreRecord rec, CharSequence sql, Object params) throws Exception {
        rec.clear();
        StoreRecord tmp = loadQueryRecord(sql, params);
        rec.setValues(tmp);
        return rec;
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

    public StoreLoader createStoreLoader(String name) {
        return getStoreService().createStoreLoader(name);
    }

    public StoreCalcField createStoreCalcField(String name) {
        return getStoreService().createStoreCalcField(name);
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

    public SqlText createSqlText(Conf conf, Object context) {
        return getSqlService().createSqlText(conf, context);
    }

    public SqlBuilder createSqlBuilder() {
        return getSqlService().createSqlBuilder();
    }

    ////// IOutData

    public void outMap(Map m, boolean showClass) {
        OutMapSaver sv = new OutMapSaver(m, showClass);
        String s = sv.save().toString();
        System.out.println(s);
    }

    public void outMap(Map m) {
        outMap(m, false);
    }

    public void outJson(Object data) {
        String s = UtJson.toJson(data, true);
        System.out.println(s);
    }

    public void outTable(Object data, int limit) {
        UtOutTable.outTable(data, limit);
    }

    public void outTable(Object data) {
        outTable(data, -1);
    }

    public void outTableList(Collection lst, int limit) {
        UtOutTable.outTableList(lst, limit);

    }

    public void outTableList(Collection lst) {
        outTableList(lst, -1);
    }

    ////// IMdbGenId

    private GenIdService getGenIdService() {
        if (genIdService == null) {
            genIdService = getModel().bean(GenIdService.class);
        }
        return genIdService;
    }

    public GenId getGenId(String genIdName) {
        return getGenIdService().getGenId(genIdName).withMdb(this);
    }

    public GenId getGenId(String genIdName, long cacheSize) {
        return getGenIdService().getGenId(genIdName, cacheSize).withMdb(this);
    }

    public long getNextId(String genIdName) {
        return getGenId(genIdName).getNextId();
    }

    ////// IMdbRec

    private long getReqId(Map params) {
        long id = UtCnv.toLong(params.get(FIELD_ID));
        if (id == 0) {
            throw new XError("Поле id должно иметь не нулевое значение");
        }
        return id;
    }

    private long getReqId(StoreRecord params) {
        long id = UtCnv.toLong(params.get(FIELD_ID));
        if (id == 0) {
            throw new XError("Поле id должно иметь не нулевое значение");
        }
        return id;
    }

    public long insertRec(String tableName, Map params, boolean generateId) throws Exception {
        Map p = params;
        long id = UtCnv.toLong(params.get(FIELD_ID));
        if (id == 0 || generateId) {
            // генерим по имени таблицы
            id = getNextId(tableName);
            p = new HashMap<>(params);
            p.put(FIELD_ID, id);
        }

        String sql = createSqlBuilder().makeSqlInsert(tableName, p);
        execQuery(sql, p);
        //
        return id;
    }

    public long insertRec(String tableName, StoreRecord params, boolean generateId) throws Exception {
        return insertRec(tableName, params.getValues(), generateId);
    }

    public void updateRec(String tableName, Map params) throws Exception {
        getReqId(params);  // проверяем, что id присутсвует

        SqlBuilder sqlBuilder = createSqlBuilder();

        List<String> flds = sqlBuilder.makeFieldList(params);
        flds.remove(FIELD_ID);

        String sql = sqlBuilder.makeSqlUpdate(tableName, flds, FIELD_ID);
        execQuery(sql, params);
    }

    public void updateRec(String tableName, StoreRecord params) throws Exception {
        getReqId(params);  // проверяем, что id присутсвует

        SqlBuilder sqlBuilder = createSqlBuilder();

        List<String> flds = sqlBuilder.makeFieldList(params);
        flds.remove(FIELD_ID);

        String sql = sqlBuilder.makeSqlUpdate(tableName, flds, FIELD_ID);
        execQuery(sql, params);
    }

    public void deleteRec(String tableName, Map params) throws Exception {
        String sql = createSqlBuilder().makeSqlDelete(tableName, params);
        execQuery(sql, params);
    }

    public void deleteRec(String tableName, long id) throws Exception {
        String sql = createSqlBuilder().makeSqlDelete(tableName, FIELD_ID);
        execQuery(sql, id);
    }

    ////// validate errors

    public void setValidateErrors(ValidateErrors validateErrors) {
        this.validateErrors = validateErrors;
    }

    public ValidateErrors getValidateErrors() {
        if (this.validateErrors == null) {
            this.validateErrors = ValidateErrors.create();
        }
        return validateErrors;
    }

    ////// IMdbValidate

    private ValidatorService getValidatorService() {
        if (validatorService == null) {
            validatorService = getModel().bean(ValidatorService.class);
        }
        return validatorService;
    }

    public boolean validate(Object data, String validatorName, Map attrs) throws Exception {
        return getValidatorService().validatorExec(this, data, validatorName, attrs);
    }

    public boolean validateRecord(Object data, Map attrs) throws Exception {
        return getValidatorService().validatorExec(this, data, "record", attrs);
    }

    public boolean validateField(Object data, String fieldName, Map attrs) throws Exception {
        Map<String, Object> attrs2 = new HashMap<>();
        if (attrs != null) {
            attrs2.putAll(attrs);
        }
        attrs2.put("field", fieldName);
        return getValidatorService().validatorExec(this, data, "field", attrs2);
    }

}
