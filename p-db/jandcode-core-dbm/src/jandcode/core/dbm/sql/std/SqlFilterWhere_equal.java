package jandcode.core.dbm.sql.std;

import jandcode.core.dbm.sql.*;

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
