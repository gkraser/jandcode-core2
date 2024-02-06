package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.apx.dbm.sqlfilter.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

public class SqlFilterContextImpl implements SqlFilterContext {

    private String name;
    private SqlText sql;
    private SqlFilter sqlFilter;
    private SqlFilterWhere sqlFilterWhere;
    private MapFilterValue filterValue;

    public SqlFilterContextImpl(SqlText sql, SqlFilter sqlFilter, SqlFilterWhere sqlFilterWhere) {
        this.sql = sql;
        this.sqlFilter = sqlFilter;
        this.sqlFilterWhere = sqlFilterWhere;
        this.name = sqlFilterWhere.getName();
        if (UtString.empty(this.name)) {
            this.name = "noname";
        }
        if (sqlFilterWhere.getAttrs().containsKey("value")) {
            this.filterValue = new MapFilterValueImpl(getKey(), sqlFilterWhere.getAttrs().get("value"));
        }
    }

    protected MapFilterValue getFilterValue() {
        if (filterValue == null) {
            throw new XError("value not assigned");
        }
        return filterValue;
    }

    boolean hasValue() {
        return filterValue != null;
    }

    public String getName() {
        return name;
    }

    ////// MapFilterValue

    public IVariantMap getProps() {
        return getFilterValue().getProps();
    }

    public String getKey() {
        String s = getAttrs().getString("key");
        if (UtString.empty(s)) {
            return getName();
        }
        return s;
    }

    public Object getValue() {
        return getFilterValue().getValue();
    }

    public boolean isEmpty() {
        return getFilterValue().isEmpty();
    }

    void assignValue(Object v) {
        this.filterValue = new MapFilterValueImpl(getKey(), v);
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

    //////

    public IVariantMap getAttrs() {
        return sqlFilterWhere.getAttrs();
    }

    public String getWherePlace() {
        return getAttrs().getString("wherePlace", null);
    }

    public String getSqlField() {
        return getAttrs().getString("sqlField", getName());
    }

    //////

    public SqlFilter getSqlFilter() {
        return sqlFilter;
    }

    public SqlText getSql() {
        return sql;
    }

    public IVariantMap getParams() {
        return sqlFilter.getParams();
    }

    public void addWhere(String where) {
        this.sql.addWhere(getWherePlace(), where);
    }

    public void setParam(String name, Object value) {
        getParams().setValue(name, value);
    }

    //////

    public void addPart(String partName, String partText) {
        this.sql.addPart(partName, partText);
    }

}
