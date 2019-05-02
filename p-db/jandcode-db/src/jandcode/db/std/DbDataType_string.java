package jandcode.db.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class DbDataType_string extends BaseDbDataType {

    public DbDataType_string() {
        setDataType(VariantDataType.STRING);
    }

    public Value getValue(ResultSet rs, int columnIdx) throws Exception {
        boolean isNull = false;
        String value = rs.getString(columnIdx);
        if (rs.wasNull()) {
            value = "";
            isNull = true;
        }
        return createValue(value, isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setString(paramIdx, UtCnv.toString(value));
    }

}
