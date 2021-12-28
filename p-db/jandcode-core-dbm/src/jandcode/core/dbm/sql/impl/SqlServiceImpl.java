package jandcode.core.dbm.sql.impl;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

public class SqlServiceImpl extends BaseModelMember implements SqlService {

    public SqlText createSqlText(String sql) {
        return new SqlTextImpl(getModel(), sql);
    }

    public SqlText createSqlText(Conf conf) {
        try {
            String sql = SqlConfUtils.loadSqlTextFromConf(conf, getModel());
            return createSqlText(sql);
        } catch (Exception e) {
            throw new XErrorMark(e, conf.origin().toString());
        }
    }
}
