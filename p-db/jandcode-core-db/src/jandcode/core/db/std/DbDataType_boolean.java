package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.sql.*;

/**
 * Значение boolean.
 * Читаем как строку, но храним число 0/1.
 */
public class DbDataType_boolean extends BaseDbDataType {

    public DbDataType_boolean() {
        setDataType(VariantDataType.BOOLEAN);
    }

    public Value getValue(ResultSet rs, int columnIdx) throws Exception {
        boolean isNull = false;
        String value = rs.getString(columnIdx);
        if (rs.wasNull()) {
            value = "false";
            isNull = true;
        }
        return createValue(UtCnv.toInt(UtCnv.toBoolean(value)), isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setBoolean(paramIdx, UtCnv.toBoolean(value));
    }

}
