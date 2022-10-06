package jandcode.core.dbm.sql.impl;

import jandcode.commons.variant.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

public class SqlFilterWhereContextImpl implements SqlFilterWhereContext {

    private SqlText sql;
    private SqlFilter sqlFilter;
    private SqlFilterWhere sqlFilterWhere;
    private MapFilterValue filterValue;

    public SqlFilterWhereContextImpl(SqlText sql, SqlFilter sqlFilter, SqlFilterWhere wh, Object v) {
        this.sql = sql;
        this.sqlFilter = sqlFilter;
        this.sqlFilterWhere = wh;
        this.filterValue = new MapFilterValueImpl(wh.getKey(), v);
    }

    protected MapFilterValue getFilterValue() {
        return filterValue;
    }

    ////// MapFilterValue

    public IVariantMap getProps() {
        return getFilterValue().getProps();
    }

    public String getKey() {
        return getFilterValue().getKey();
    }

    public Object getValue() {
        return getFilterValue().getValue();
    }

    public List<String> getValueList() {
        return getFilterValue().getValueList();
    }

    public Set<Long> getValueIds() {
        return getFilterValue().getValueIds();
    }

    public String paramName(String suffix) {
        return getFilterValue().paramName(suffix);
    }

    ////// ISqlFilterWhere

    public IVariantMap getAttrs() {
        return getSqlFilterWhere().getAttrs();
    }

    public String getWherePlace() {
        return getSqlFilterWhere().getWherePlace();
    }

    public String getSqlField() {
        return getSqlFilterWhere().getSqlField();
    }

    //////

    public SqlFilter getSqlFilter() {
        return sqlFilter;
    }

    public SqlFilterWhere getSqlFilterWhere() {
        return sqlFilterWhere;
    }

    public SqlText getSql() {
        return sql;
    }

    public IVariantMap getParams() {
        return sqlFilter.getParams();
    }

    public void addWhere(String where) {
        this.sql.addWhere(this.sqlFilterWhere.getWherePlace(), where);
    }

    public void setParam(String name, Object value) {
        getParams().setValue(name, value);
    }

}
