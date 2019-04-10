package jandcode.db.postgresql;

import jandcode.db.impl.dbt.*;
import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;

import java.sql.*;
import java.time.*;

public class PostgresqlDbt_datetime extends BaseDbt {

    public PostgresqlDbt_datetime() {
        setDatatype(VariantDataType.DATETIME);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        XDateTime res = null;
        LocalDateTime s = rs.getObject(columnIdx, LocalDateTime.class);
        if (rs.wasNull()) {
            return null;
        } else {
            res = UtDateTime.create(s);
        }
        return res;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        XDateTime dt = UtCnv.toDateTime(value);
        // милисекунды игнорируем
        dt = dt.clearMSec();
        //
        st.setObject(paramIdx, dt.toJavaLocalDateTime());
    }

}
