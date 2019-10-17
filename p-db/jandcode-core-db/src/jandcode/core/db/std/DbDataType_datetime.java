package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;

import java.sql.*;
import java.time.*;

public class DbDataType_datetime extends BaseDbDataType {

    public DbDataType_datetime() {
        setDataType(VariantDataType.DATETIME);
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
        XDateTime dtValue = UtCnv.toDateTime(value);
        dtValue = dtValue.clearMSec();
        return createValue(dtValue, isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        XDateTime dt = UtCnv.toDateTime(value);
        dt = dt.clearMSec();
        LocalDateTime jdt = dt.toJavaLocalDateTime();
        st.setObject(paramIdx, jdt);
    }

}
