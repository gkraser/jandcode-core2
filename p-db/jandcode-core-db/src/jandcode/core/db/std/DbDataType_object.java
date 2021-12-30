package jandcode.core.db.std;

import jandcode.commons.variant.*;

import java.sql.*;

public class DbDataType_object extends BaseDbDataType {

    public DbDataType_object() {
        setDataType(VariantDataType.OBJECT);
    }

    public Value getValue(ResultSet rs, int columnIdx) throws Exception {
        Object value = rs.getObject(columnIdx);
        if (rs.wasNull()) {
            return createValue(null);
        }
        return createValue(value);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setObject(paramIdx, value);
    }

}
