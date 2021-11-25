package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;

import java.sql.*;
import java.time.*;

public class DbDataType_date extends BaseDbDataType {

    public DbDataType_date() {
        setDataType(VariantDataType.DATE);
    }

    public Value getValue(ResultSet rs, int columnIdx) throws Exception {
        boolean isNull = false;
        Object value = rs.getObject(columnIdx);
        if (rs.wasNull()) {
            value = null;
            isNull = true;
        } else if (value instanceof Timestamp) {
            value = ((Timestamp) value).toLocalDateTime();
        } else if (value instanceof Date) {
            value = ((Date) value).toLocalDate();
        }
        XDate dtValue = UtCnv.toDate(value);
        return createValue(dtValue, isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        XDate dt = UtCnv.toDate(value);
        LocalDate jdt = dt.toJavaLocalDate();
        st.setObject(paramIdx, jdt);
    }

}
