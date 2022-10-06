package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.core.apx.dbm.sqlfilter.*;

import java.util.*;

public class SqlFilterWhereImpl implements SqlFilterWhere {

    private IVariantMap attrs = new VariantMap();
    private SqlFilterBuilder builder;
    private String name;

    public SqlFilterWhereImpl(String name, SqlFilterBuilder builder, Map attrs) {
        this.name = name;
        this.builder = builder;
        if (attrs != null) {
            this.attrs.putAll(attrs);
        }
        if (UtString.empty(this.name)) {
            this.name = "noname";
        }
    }

    public String getName() {
        return name;
    }

    public SqlFilterBuilder getBuilder() {
        return builder;
    }

    public IVariantMap getAttrs() {
        return attrs;
    }

}
