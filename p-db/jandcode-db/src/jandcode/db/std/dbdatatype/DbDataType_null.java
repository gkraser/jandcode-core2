package jandcode.db.std.dbdatatype;

import jandcode.commons.variant.*;

import java.sql.*;

public class DbDataType_null extends BaseDbDataType {

    public DbDataType_null() {
        setDataType(VariantDataType.OBJECT);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        return null;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setObject(paramIdx, null);
    }

}
