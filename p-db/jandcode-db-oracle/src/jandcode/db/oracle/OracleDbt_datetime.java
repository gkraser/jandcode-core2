package jandcode.db.oracle;

import jandcode.db.impl.dbt.*;
import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;

import java.sql.*;

/*
Внутренне представление даты для oracle (getBytes())

byte 1: century + 100
byte 2: year + 100
byte 3: month
byte 4: day of month
byte 5: hour + 1
byte 6: minute + 1
byte 7: second + 1

*/

public class OracleDbt_datetime extends BaseDbt {

    protected XDateTimeFormatter FMT_DATE = UtDateTime.createFormatter("yyyy-MM-dd");
    protected XDateTimeFormatter FMT_DATETIME = UtDateTime.createFormatter("yyyy-MM-dd HH:mm:ss");

    public OracleDbt_datetime() {
        setDatatype(VariantDataType.DATETIME);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        XDateTime res = null;
        byte[] a = rs.getBytes(columnIdx);
        if (rs.wasNull()) {
            return null;
        } else {
            try {
                if (a.length != 7) {
                    return null;
                }
                int b0 = a[0] & 0xFF;
                int b1 = a[1] & 0xFF;
                res = UtDateTime.create(
                        (b0 - 100) * 100 + (b1 - 100),
                        a[2],
                        a[3],
                        a[4] - 1,
                        a[5] - 1,
                        a[6] - 1,
                        0
                );
            } catch (Exception e) {
                return null;
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
