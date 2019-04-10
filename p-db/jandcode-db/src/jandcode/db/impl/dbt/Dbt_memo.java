package jandcode.db.impl.dbt;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class Dbt_memo extends BaseDbt {

    public Dbt_memo() {
        setDatatype(VariantDataType.STRING);
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
