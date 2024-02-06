package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.apx.dbm.sqlfilter.*;

import java.util.*;

public class SqlFilterWhereImpl implements SqlFilterWhere {

    private IVariantMap attrs = new VariantMap();
    private String name;
    private ChainBuilder chainBuilder = new ChainBuilder();

    static class ChainBuilder implements SqlFilterBuilder {

        List<SqlFilterBuilder> builders = new ArrayList<>();

        public void buildWhere(SqlFilterContext ctx) {
            for (SqlFilterBuilder b : builders) {
                b.buildWhere(ctx);
            }
        }
    }

    public SqlFilterWhereImpl(String name, SqlFilterBuilder builder, Map attrs) {
        this.name = name;
        addBuilder(builder);
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
        return chainBuilder;
    }

    public IVariantMap getAttrs() {
        return attrs;
    }

    public SqlFilterWhere addBuilder(SqlFilterBuilder builder) {
        if (builder == null) {
            throw new XError("builder not assigned");
        }
        this.chainBuilder.builders.add(builder);
        return this;
    }

}
