package jandcode.db.oracle;

import jandcode.db.base.*;

import java.sql.*;

public class OracleDbDriver extends BaseDbDriver {

    protected String getDbDataTypeName(ResultSetMetaData md, int colIdx) throws Exception {
        String dt = super.getDbDataTypeName(md, colIdx);
        if ("double".equals(dt)) {
            int pr = md.getPrecision(colIdx);
            int sc = md.getScale(colIdx);
            if (pr > 0 && sc == 0) {
                if (pr <= 5) {
                    dt = "smallint";
                } else if (pr <= 10) {
                    dt = "int";
                } else if (pr <= 19) {
                    dt = "long";
                }
            }
        }
        return dt;
    }

}
