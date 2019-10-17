package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.variant.*;

import java.io.*;
import java.sql.*;

public class DbDataType_blob extends BaseDbDataType {

    protected static byte[] EMPTY_BYTEARRAY = new byte[0];

    public DbDataType_blob() {
        setDataType(VariantDataType.BLOB);
    }

    public Value getValue(ResultSet rs, int columnIdx) throws Exception {
        boolean isNull = false;
        Object value = rs.getObject(columnIdx);
        if (rs.wasNull()) {
            value = EMPTY_BYTEARRAY;
            isNull = true;

        } else if (value instanceof Blob) { // читаем блоб
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
            value = EMPTY_BYTEARRAY;
        }

        return createValue(value, isNull);
    }

    public void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception {
        st.setBytes(paramIdx, UtCnv.toByteArray(value));
    }


}
