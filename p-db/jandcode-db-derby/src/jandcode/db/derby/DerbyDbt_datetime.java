package jandcode.db.derby;

import jandcode.db.impl.dbt.*;
import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class DerbyDbt_datetime extends BaseDbt {

    protected XDateTimeFormatter FMT_DATE = UtDateTime.createFormatter("yyyy-MM-dd");
    protected XDateTimeFormatter FMT_DATETIME = UtDateTime.createFormatter("yyyy-MM-dd HH:mm:ss.S");
    protected int maxDateStrSize = 10;

    public DerbyDbt_datetime() {
        setDatatype(VariantDataType.DATETIME);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        XDateTime res = null;
        String s = rs.getString(columnIdx);
        if (rs.wasNull()) {
            return null;
        } else {
            XDateTimeFormatter fmt = FMT_DATE;
            if (s.length() > maxDateStrSize) {
                fmt = FMT_DATETIME;
            }
            try {
                res = UtDateTime.create(s, fmt).clearMSec();
            } catch (Exception e) {
                res = null;
            }
        }
        return res;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        XDateTime dt = UtCnv.toDateTime(value);
        // милисекунды игнорируем
        dt = dt.clearMSec();
        // именно date-time, без вариантов
        XDateTimeFormatter fmt = FMT_DATETIME;
        //
        st.setString(paramIdx, dt.toString(fmt));
    }

}
