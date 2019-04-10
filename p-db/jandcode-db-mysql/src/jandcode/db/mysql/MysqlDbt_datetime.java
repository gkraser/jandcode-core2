package jandcode.db.mysql;

import jandcode.db.impl.dbt.*;
import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;

import java.sql.*;

public class MysqlDbt_datetime extends BaseDbt {

    protected XDateTimeFormatter FMT_DATE = UtDateTime.createFormatter("yyyy-MM-dd");
    protected XDateTimeFormatter FMT_DATETIME = UtDateTime.createFormatter("yyyy-MM-dd HH:mm:ss");
    protected int maxDateStrSize = 10;

    public MysqlDbt_datetime() {
        setDatatype(VariantDataType.DATETIME);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        XDateTime res = null;
        byte[] a = rs.getBytes(columnIdx);
        if (rs.wasNull() || a == null) {
            return null;
        } else {
            String s = new String(a);
            //
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

        XDateTimeFormatter fmt = FMT_DATE;
        if (dt.hasTime()) {
            // время имеется
            fmt = FMT_DATETIME;
        }

        st.setString(paramIdx, dt.toString(fmt));
    }

}