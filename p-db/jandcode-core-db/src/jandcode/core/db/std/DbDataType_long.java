package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class DbDataType_long extends BaseDbDataType {

    public DbDataType_long() {
        setDataType(VariantDataType.LONG);
    }

    public Value getValue(ResultSet rs, int columnIdx) throws Exception {
        boolean isNull = false;
        long value = rs.getLong(columnIdx);
        if (rs.wasNull()) {
            value = 0;
            isNull = true;
        }
        return createValue(value, isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setLong(paramIdx, UtCnv.toLong(value));
    }

}
