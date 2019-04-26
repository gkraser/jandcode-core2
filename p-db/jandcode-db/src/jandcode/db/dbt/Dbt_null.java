package jandcode.db.dbt;

import jandcode.commons.variant.*;

import java.sql.*;

public class Dbt_null extends BaseDbt {

    public Dbt_null() {
        setDataType(VariantDataType.OBJECT);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        return null;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setObject(paramIdx, null);
    }

}
