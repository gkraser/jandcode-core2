package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.variant.*;
import jandcode.core.apx.dbm.sqlfilter.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

public class SqlFilterImpl extends BaseModelMember implements SqlFilter {

    private Map<String, Object> origParams;
    private String origSql;
    private IVariantMap params = new VariantMap();
    private List<SqlFilterWhere> wheres = new ArrayList<>();
    private SqlText sql;


    public SqlFilterImpl(Model model, String origSql, Map<String, Object> origParams) {
        setModel(model);
        this.origParams = origParams;
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
    }

    private void prepare() {
        SqlText tmpSql = getModel().bean(SqlService.class).createSqlText(getOrigSql());
        // todo собстенно работа

        // для каждого зарегистрированного where проверяем,
        // есть ли для него информация в параметрах, если есть - используем его

        for (SqlFilterWhere wh : this.wheres) {
            SqlFilterContextImpl ctx = new SqlFilterContextImpl(tmpSql, this, wh);
            String key = ctx.getKey();
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
            wh.getBuilder().buildWhere(ctx);
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
}

