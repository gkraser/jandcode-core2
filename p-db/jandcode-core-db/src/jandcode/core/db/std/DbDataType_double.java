package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class DbDataType_double extends BaseDbDataType {

    public DbDataType_double() {
        setDataType(VariantDataType.DOUBLE);
    }

    public Value getValue(ResultSet rs, int columnIdx) throws Exception {
        boolean isNull = false;
        double value = rs.getDouble(columnIdx);
        if (rs.wasNull()) {
            value = 0.0;
            isNull = true;
        }
        return createValue(value, isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setDouble(paramIdx, UtCnv.toDouble(value));
    }

}
