package jandcode.db.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class DbDataType_double extends BaseDbDataType {

    public DbDataType_double() {
        setDataType(VariantDataType.DOUBLE);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        Object value = rs.getDouble(columnIdx);
        if (rs.wasNull()) {
            value = null;
        }
        return value;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setDouble(paramIdx, UtCnv.toDouble(value));
    }

}
