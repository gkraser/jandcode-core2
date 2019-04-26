package jandcode.db.dbt;

import jandcode.commons.error.*;
import jandcode.commons.variant.*;

import java.sql.*;

/**
 * ЭТА реализация - просто заглушка. Используейте специфический подход к дате и времени
 * для каждого драйвера
 */
public class Dbt_datetime extends BaseDbt {

    public Dbt_datetime() {
        setDataType(VariantDataType.DATETIME);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        throw new XError("Not implemented getValue for date");
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        throw new XError("Not implemented setValue for date");
    }

}
