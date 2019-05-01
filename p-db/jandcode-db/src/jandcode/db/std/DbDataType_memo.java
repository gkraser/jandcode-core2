package jandcode.db.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class DbDataType_memo extends BaseDbDataType {

    public DbDataType_memo() {
        setDataType(VariantDataType.STRING);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        Object value = rs.getString(columnIdx);
        if (rs.wasNull()) {
            value = null;
        }
        return value;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setString(paramIdx, UtCnv.toString(value));
    }

}
