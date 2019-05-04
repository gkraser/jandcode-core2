package jandcode.db.postgresql;

import jandcode.commons.variant.*;
import jandcode.db.*;
import jandcode.db.std.*;

import java.sql.*;
import java.util.*;

public class PostgresqlDbDriver extends BaseDbDriver {

    public void assignStatementParams(PreparedStatement statement, IVariantNamed paramValues, List<String> paramNames) throws Exception {
        // postgre не конвертирует автоматом string->int, поэтому тут берем типы параметров за основу
        ParameterMetaData md = statement.getParameterMetaData();
        for (int i = 0; i < paramNames.size(); i++) {
            String paramName = paramNames.get(i);
            int paramIdx = i + 1;

            VariantDataType dt = paramValues.getDataType(paramName);

            if (paramValues.isNull(paramName)) {
                setNullParam(statement, paramIdx, dt);
            } else {
                Object value = paramValues.getValue(paramName);
                int ptyp = md.getParameterType(paramIdx);
                DbDataType dbdt = getDbDataType(ptyp);
                dbdt.setValue(statement, paramIdx, value);
            }
        }
    }

    protected String getDbDataTypeName(ResultSetMetaData md, int colIdx) throws Exception {
        String dt = super.getDbDataTypeName(md, colIdx);
        if ("string".equals(dt)) {
            String ctn = md.getColumnTypeName(colIdx);
            if ("text".equals(ctn)) {
                dt = "memo";
            }
        }
        return dt;
    }

}
