package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.apx.dbm.sqlfilter.*;
import jandcode.core.apx.store.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.sql.*;
import jandcode.core.store.*;

import java.util.*;

public class SqlFilterImpl extends BaseModelMember implements SqlFilter {

    private Mdb mdb;
    private Map<String, Object> origParams = new LinkedHashMap<>();
    private String origSql;
    private IVariantMap params = new VariantMap();
    private List<SqlFilterWhere> wheres = new ArrayList<>();
    private SqlText sql;
    private Map<String, String> orderBy = new LinkedHashMap<>();
    private Paginate paginate;
    // id
    private boolean needOneRec;
    private boolean hasIdWhere;
    private Object idValue;


    public SqlFilterImpl(Mdb mdb, String origSql, Map<String, Object> origParams) {
        this.mdb = mdb;
        setModel(mdb.getModel());
        if (origParams != null) {
            this.origParams.putAll(origParams);
        }
        this.origSql = origSql;
        // переносим параметры
        MapFilter mf = new MapFilter(origParams);
        for (var key : mf.keySet()) {
            MapFilterValue v = mf.getValue(key);
            this.params.setValue(key, v.getValue());
        }
    }

    public Map<String, Object> getOrigParams() {
        return origParams;
    }

    public String getOrigSql() {
        return origSql;
    }

    public IVariantMap getParams() {
        return params;
    }

    private void reset() {
        this.sql = null;
        this.paginate = null;
        this.hasIdWhere = false;
        this.idValue = null;
        this.needOneRec = false;
    }

    private void prepare() {
        SqlText tmpSql = getModel().bean(SqlService.class).createSqlText(getOrigSql());

        // для каждого зарегистрированного where проверяем,
        // есть ли для него информация в параметрах, если есть - используем его
        for (SqlFilterWhere wh : this.wheres) {
            SqlFilterContextImpl ctx = new SqlFilterContextImpl(tmpSql, this, wh);
            String key = ctx.getKey();
            if (SqlFilterConsts.id.equals(key)) {
                this.hasIdWhere = true; // имеется фильтр с именем id
            }
            if (!ctx.hasValue() && !getOrigParams().containsKey(key)) {
                // нет значения для фильтра
                continue;
            }
            // этот where нужен
            if (!ctx.hasValue()) {
                // если явно не назначено значение, то берем из параметров
                Object v = getOrigParams().get(key);
                ctx.assignValue(v);
            }
            // пустые значения пропускаем
            if (ctx.isEmpty()) {
                continue;
            }
            //
            wh.getBuilder().buildWhere(ctx);
            //
            if (SqlFilterConsts.id.equals(key)) {
                this.idValue = ctx.getValue(); // забираем значение id для фильтра id
                if (ctx.isEmpty()) {
                    this.idValue = null;
                }
            }
        }

        // проверка на id
        if (getOrigParams().containsKey(SqlFilterConsts.id)) {
            // имеется параметр id
            if (!this.hasIdWhere) {
                throw new XError("В SqlFilter имеется параметр id, но нет фильтра с именем id");
            }
            if (this.idValue == null) {
                throw new XError("В SqlFilter значение параметра id=null");
            }
            this.needOneRec = true;
        }

        // orderBy
        if (getOrigParams().containsKey(SqlFilterConsts.orderBy)) {
            String key = SqlFilterConsts.orderBy;
            MapFilterValue pv = new MapFilterValueImpl(key, getOrigParams().get(key));
            if (!UtString.empty(pv.getString())) {
                String expr = this.orderBy.get(pv.getString());
                if (!UtString.empty(expr)) {
                    tmpSql.replaceOrderBy(expr);
                }
            }
        }

        // paginate
        if (getOrigParams().containsKey(SqlFilterConsts.paginate)) {
            String key = SqlFilterConsts.paginate;
            String pfx = key + "__";

            MapFilterValue pv = new MapFilterValueImpl(key, getOrigParams().get(key));
            tmpSql.paginate(true);
            tmpSql.paginateParamsPrefix(pfx);
            this.paginate = new Paginate(pv.getProps());
            getParams().put(pfx + "offset", this.paginate.getOffset());
            getParams().put(pfx + "limit", this.paginate.getLimit());
        }

        this.sql = tmpSql;
    }

    public SqlText getSql() {
        if (this.sql == null) {
            prepare();
        }
        return this.sql;
    }

    public SqlText getSqlCount() {
        SqlText currSql = getSql();
        return currSql.asCountSqlText("cnt");
    }

    //////

    protected SqlFilterWhere addWhereInst(String name, SqlFilterBuilder builder, Map attrs) {
        reset();
        SqlFilterWhere inst = new SqlFilterWhereImpl(name, builder, attrs);
        this.wheres.add(inst);
        return inst;
    }

    public SqlFilterWhere addWhere(String name, SqlFilterBuilder builder) {
        return addWhereInst(name, builder, null);
    }

    public SqlFilterWhere addWhere(String name, Map attrs, SqlFilterBuilder builder) {
        return addWhereInst(name, builder, attrs);
    }

    public SqlFilterWhere addWhere(String name, String builder) {
        return addWhere(name, builder, null);
    }

    public SqlFilterWhere addWhere(String name, String builder, Map attrs) {
        SqlFilterBuilder b = getModel().bean(SqlFilterService.class).createSqlFilterBuilder(builder);
        return addWhereInst(name, b, attrs);
    }

    //////

    public void addOrderBy(String name, String orderBy) {
        reset();
        this.orderBy.put(name, orderBy);
    }

    public void addOrderBy(Map<String, String> data) {
        reset();
        if (data != null) {
            this.orderBy.putAll(data);
        }
    }

    //////

    private void prepareLoadedStore(Store st) throws Exception {
        if (this.needOneRec) {
            //  резульате должна быть только одна запись
            if (st.size() == 0) {
                throw new XError("Не найдена запись #{0}", this.idValue);
            }
            if (st.size() > 1) {
                throw new XError("Для #{0} найдено более одной записи ({1})", this.idValue, st.size());
            }
        }
        if (this.paginate != null && !this.needOneRec) {
            // требуется пагинация
            Paginate pag = new Paginate(this.paginate);
            //
            StoreRecord rec = mdb.loadQueryRecord(getSqlCount());
            pag.setTotal(rec.getInt("cnt"));
            //
            ApxStoreUtils.setPaginate(st, pag);
        }
    }

    public void load(Store st) throws Exception {
        mdb.loadQuery(st, getSql(), getParams());
        prepareLoadedStore(st);
    }

    public Store load() throws Exception {
        Store st = mdb.loadQuery(getSql(), getParams());
        prepareLoadedStore(st);
        return st;
    }

}

