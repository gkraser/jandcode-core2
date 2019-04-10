package jandcode.db.impl.dbt;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class Dbt_long extends BaseDbt {

    public Dbt_long() {
        setDatatype(VariantDataType.LONG);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        Object value = rs.getLong(columnIdx);
        if (rs.wasNull()) {
            value = null;
        }
        return value;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setLong(paramIdx, UtCnv.toLong(value));
    }

}
