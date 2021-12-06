package jandcode.core.dbm.sql.impl;

import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

public class SqlServiceImpl extends BaseModelMember implements SqlService {

    public SqlText createSqlText(String sql) {
        return new SqlTextImpl(getModel(), sql);
    }

}
