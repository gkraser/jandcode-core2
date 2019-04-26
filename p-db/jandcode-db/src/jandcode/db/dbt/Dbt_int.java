package jandcode.db.dbt;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class Dbt_int extends BaseDbt {

    public Dbt_int() {
        setDataType(VariantDataType.INT);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        Object value = rs.getInt(columnIdx);
        if (rs.wasNull()) {
            value = null;
        }
        return value;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setInt(paramIdx, UtCnv.toInt(value));
    }

}
