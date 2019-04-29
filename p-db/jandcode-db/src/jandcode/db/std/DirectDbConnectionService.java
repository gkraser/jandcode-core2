package jandcode.db.std;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.db.*;

import java.sql.*;
import java.util.*;

/**
 * Реализация соединения без пулов, прямое.
 */
public class DirectDbConnectionService extends BaseDbConnectionService {

    public Connection connect() {
        String s;

        //
        DbSource dbSource = getDbSource();
        IVariantMap dbsProps = dbSource.getProps();

        //
        try {
            Properties props = new Properties();
            s = dbsProps.getString(DbSourcePropsConsts.username, null);
            if (s != null) {
                props.put("user", s);
            }
            s = dbsProps.getString(DbSourcePropsConsts.password, null);
            if (s != null) {
                // возможно пустой пароль
                props.put("password", s);
            }
            props.putAll(dbSource.getProps("conn", false));
            s = dbsProps.getString(DbSourcePropsConsts.url);
            Connection conn = DriverManager.getConnection(s, props);

            String databaseName = dbsProps.getString(DbSourcePropsConsts.database);
            if (!UtString.empty(databaseName)) {
                conn.setCatalog(databaseName);
            }

            // init sql
            List<String> sqls = getInitConnectionSqls();
            if (sqls.size() > 0) {
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

    public void disconnectAll() {
    }

}
