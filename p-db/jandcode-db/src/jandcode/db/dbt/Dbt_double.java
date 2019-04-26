package jandcode.db.dbt;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class Dbt_double extends BaseDbt {

    public Dbt_double() {
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
