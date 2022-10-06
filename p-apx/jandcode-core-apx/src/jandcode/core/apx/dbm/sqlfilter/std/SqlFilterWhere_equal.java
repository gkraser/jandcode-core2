package jandcode.core.apx.dbm.sqlfilter.std;

import jandcode.core.apx.dbm.sqlfilter.*;

/**
 * =
 */
public class SqlFilterWhere_equal extends BaseSqlFilterWhere {

    public void buildWhere(SqlFilterWhereContext ctx) {
        String pname = ctx.paramName("value");
        Object v = ctx.getValue();
        //
        ctx.addWhere(ctx.getSqlField() + "=:" + pname);
        ctx.setParam(pname, v);
    }

}
