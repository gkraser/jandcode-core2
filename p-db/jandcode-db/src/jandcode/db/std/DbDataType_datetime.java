package jandcode.db.std;

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
        }
        return createValue(UtCnv.toDateTime(value), isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        XDateTime dt = UtCnv.toDateTime(value);

        LocalDateTime jdt = dt.toJavaLocalDateTime();
        st.setObject(paramIdx, jdt);

//        if (dt.hasTime()){
//        } else {
//
//        }
//
//        //dt.to
//        st.setObject(paramIdx, UtCnv.toDateTime(value));
    }

}
