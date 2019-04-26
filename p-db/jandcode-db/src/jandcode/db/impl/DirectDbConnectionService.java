package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.db.*;

import java.sql.*;
import java.util.*;

/**
 * Реализация соединения без пулов, прямое.
 */
public class DirectDbConnectionService extends BaseDbSourceMember implements DbConnectionService {

    public Connection connect() {
        String s;
        // проверка наличия драйвера
        DbSource dbSource = getDbSource();

        //
        try {
            Properties props = new Properties();
            s = dbSource.getProps().get(DbSourcePropsConsts.username);
            if (s != null) {
                props.put("user", s);
            }
            s = dbSource.getProps().get(DbSourcePropsConsts.password);
            if (s != null) {
                // возможно пустой пароль
                props.put("password", s);
            }
            props.putAll(dbSource.getProps("conn", false));
            s = dbSource.getProps().get(DbSourcePropsConsts.url);
            Connection conn = DriverManager.getConnection(s, props);

            String databaseName = dbSource.getProps().get(DbSourcePropsConsts.database);
            if (!UtString.empty(databaseName)) {
                conn.setCatalog(databaseName);
            }

            // init sql
            List<String> sqls = dbSource.getInitConnectionSqls();
            if (sqls != null && sqls.size() > 0) {
                Statement st = conn.createStatement();
                try {
                    for (String sql : sqls) {
                        try {
                            st.execute(sql);
                        } catch (Exception e) {
                            throw new XErrorMark(e, "initConnectionSql: " + sql);
                        }
                    }
                } finally {
                    st.close();
                }
            }

            return conn;
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new XErrorWrap(e);
            }
        }
    }

    public void disconnectAll() {
    }

}
