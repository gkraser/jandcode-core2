package jandcode.db.postgresql;

import jandcode.db.impl.dbt.*;
import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;

import java.sql.*;
import java.time.*;

public class PostgresqlDbt_date extends BaseDbt {

    public PostgresqlDbt_date() {
        setDatatype(VariantDataType.DATETIME);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        XDateTime res = null;
        LocalDate s = rs.getObject(columnIdx, LocalDate.class);
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
        st.setObject(paramIdx, dt.toJavaLocalDateTime().toLocalDate());
    }

}
