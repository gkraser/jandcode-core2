package jandcode.core.dbm.sql.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.dbm.sql.*;

public abstract class SqlFilterWhereImpl implements SqlFilterWhere, SqlFilterWhereBuilder, INamedSet {

    private IVariantMap attrs = new VariantMap();
    private String name;
    private String key;
    private String wherePlace;
    private String sqlField;

    public String getName() {
        if (UtString.empty(name)) {
            return "noname";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IVariantMap getAttrs() {
        return attrs;
    }

    public String getKey() {
        if (UtString.empty(key)) {
            return getName();
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWherePlace() {
        return wherePlace;
    }

    public void setWherePlace(String wherePlace) {
        this.wherePlace = wherePlace;
    }

    public String getSqlField() {
        if (UtString.empty(sqlField)) {
            return getName();
        }
        return sqlField;
    }

    public void setSqlField(String sqlField) {
        this.sqlField = sqlField;
    }

}
