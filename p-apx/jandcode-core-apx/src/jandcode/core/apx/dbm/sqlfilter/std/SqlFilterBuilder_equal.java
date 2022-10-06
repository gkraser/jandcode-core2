package jandcode.core.apx.dbm.sqlfilter.std;

import jandcode.core.apx.dbm.sqlfilter.*;

/**
 * =
 */
public class SqlFilterBuilder_equal implements SqlFilterBuilder {

    public void buildWhere(SqlFilterContext ctx) {
        String pname = ctx.paramName("value");
        Object v = ctx.getValue();
        //
        ctx.addWhere(ctx.getSqlField() + "=:" + pname);
        ctx.setParam(pname, v);
    }

}
