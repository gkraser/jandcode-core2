package jandcode.db.std;

import jandcode.commons.error.*;
import jandcode.commons.variant.*;

import java.sql.*;

/**
 * ЭТА реализация - просто заглушка. Используейте специфический подход к дате и времени
 * для каждого драйвера
 */
public class DbDataType_datetime extends BaseDbDataType {

    public DbDataType_datetime() {
        setDataType(VariantDataType.DATETIME);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        throw new XError("Not implemented getValue for date");
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        throw new XError("Not implemented setValue for date");
    }

}
