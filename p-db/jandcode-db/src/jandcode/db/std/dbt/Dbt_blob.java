package jandcode.db.std.dbt;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.variant.*;

import java.io.*;
import java.sql.*;

public class Dbt_blob extends BaseDbt {

    public Dbt_blob() {
        setDataType(VariantDataType.BLOB);
    }

    public Object getValue(ResultSet rs, int columnIdx) throws Exception {
        Object value = rs.getObject(columnIdx);
        if (rs.wasNull()) {
            return null;
        }

        if (value instanceof Blob) { // читаем блоб
            Blob b = (Blob) value;
            long len = b.length();
            value = b.getBytes(1, (int) len);
        } else if (value instanceof Clob) {
            Clob b = (Clob) value;
            Reader strm = b.getCharacterStream();
            try {
                StringLoader ldr = new StringLoader();
                UtLoad.fromReader(ldr, strm);
                value = ldr.getResult();
            } finally {
                strm.close();
            }
        } else if (value instanceof byte[]) {
        } else {
            value = null;
        }

        if (value instanceof byte[]) {
            if (((byte[]) value).length == 0) {
                value = null;
            }
        }

        return value;
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setBytes(paramIdx, UtCnv.toByteArray(value));
    }


}
