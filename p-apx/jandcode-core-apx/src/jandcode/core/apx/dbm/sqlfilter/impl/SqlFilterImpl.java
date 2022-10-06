package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.apx.dbm.sqlfilter.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

public class SqlFilterImpl extends BaseModelMember implements SqlFilter {

    private Map<String, Object> origParams;
    private MapFilter origMapFilter;
    private String origSql;
    private IVariantMap params = new VariantMap();
    private List<SqlFilterWhere> wheres = new ArrayList<>();
    private SqlText sql;

    /**
     * Враппер для SqlFilterWhereBuilder
     */
    public static class SqlFilterWhereBuilderWrapper extends SqlFilterWhereImpl {
        private SqlFilterWhereBuilder b;

        public SqlFilterWhereBuilderWrapper(SqlFilterWhereBuilder b) {
            this.b = b;
        }

        public void buildWhere(SqlFilterWhereContext ctx) {
            b.buildWhere(ctx);
        }
    }

    public SqlFilterImpl(Model model, String origSql, Map<String, Object> origParams) {
        setModel(model);
        this.origParams = origParams;
        this.origSql = origSql;
        // переносим параметры
        this.origMapFilter = new MapFilter(origParams);
        for (var key : this.origMapFilter.keySet()) {
            MapFilterValue v = this.origMapFilter.getValue(key);
            this.params.setValue(key, v.getValue());
        }
    }

    public Map<String, Object> getOrigParams() {
        return origParams;
    }

    protected MapFilter getOrigMapFilter() {
        return origMapFilter;
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
            String key = wh.getKey();
            if (!getOrigParams().containsKey(key)) {
                // нет значения для фильтра
                continue;
            }
            // этот where нужен
            Object v = getOrigParams().get(key);
            SqlFilterWhereContext ctx = new SqlFilterWhereContextImpl(tmpSql, this, wh, v);
            wh.buildWhere(ctx);
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

    protected void addWhereInst(String name, SqlFilterWhere inst) {
        reset();
        if (inst instanceof INamedSet nm) {
            nm.setName(name);
        }
        this.wheres.add(inst);
    }

    public SqlFilterWhere addWhere(String name, SqlFilterWhereBuilder builder) {
        SqlFilterWhere inst = new SqlFilterWhereBuilderWrapper(builder);
        addWhereInst(name, inst);
        return inst;
    }

    public SqlFilterWhere addWhere(String name, String sqlFilterWhereName) {
        SqlFilterWhere inst = getModel().bean(SqlFilterService.class).createSqlFilterWhere(sqlFilterWhereName);
        addWhereInst(name, inst);
        return inst;
    }

    public SqlFilterWhere addWhere(String name, String sqlFilterWhereName, Map attrs) {
        SqlFilterWhere inst = getModel().bean(SqlFilterService.class).createSqlFilterWhere(sqlFilterWhereName);
        if (attrs != null) {
            inst.getAttrs().putAll(attrs);
            UtReflect.getUtils().setProps(inst, attrs);
        }
        addWhereInst(name, inst);
        return inst;
    }
}

