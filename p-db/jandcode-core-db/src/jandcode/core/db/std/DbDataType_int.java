package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class DbDataType_int extends BaseDbDataType {

    public DbDataType_int() {
        setDataType(VariantDataType.INT);
    }

    public Value getValue(ResultSet rs, int columnIdx) throws Exception {
        boolean isNull = false;
        int value = rs.getInt(columnIdx);
        if (rs.wasNull()) {
            value = 0;
            isNull = true;
        }
        return createValue(value, isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setInt(paramIdx, UtCnv.toInt(value));
    }

}
