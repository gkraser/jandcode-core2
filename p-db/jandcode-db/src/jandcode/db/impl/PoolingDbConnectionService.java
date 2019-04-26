package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.db.*;
import org.apache.commons.dbcp2.*;

import java.sql.*;
import java.util.*;

/**
 * Настройка {@link BasicDataSource} производится через свойства вида:
 * dbcp.XXXX, где XXX можно посмотреть тут:
 * https://commons.apache.org/proper/commons-dbcp/configuration.html
 */
public class PoolingDbConnectionService extends BaseDbSourceMember implements DbConnectionService {

    protected BasicDataSource datasource;

    ////

    public Connection connect() {
        try {
            return getDatasource().getConnection();
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
        try {
            BasicDataSource ds = datasource;
            datasource = null;
            if (ds != null) {
                ds.close();
            }
        } catch (SQLException e) {
            throw new XErrorWrap(e);
        }
    }

    protected BasicDataSource getDatasource() {
        if (datasource == null) {
            synchronized (this) {
                if (datasource == null) {
                    try {
                        datasource = createDatasource();
                    } catch (Exception e) {
                        throw new XErrorWrap(e);
                    }
                }
            }
        }
        return datasource;
    }

    protected BasicDataSource createDatasource() throws Exception {

        DbSource dbSource = getDbSource();

        // создаем datasource с использованием свойств dbcp.XXXX
        Properties dbcpProps = new Properties();
        dbcpProps.putAll(dbSource.getProps("dbcp", false));
        BasicDataSource bds = BasicDataSourceFactory.createDataSource(dbcpProps);

        bds.setUrl(dbSource.getProps().get(DbSourcePropsConsts.url));
        String s = dbSource.getProps().get(DbSourcePropsConsts.username);
        if (s != null) {
            bds.setUsername(s);
        }
        s = dbSource.getProps().get(DbSourcePropsConsts.password);
        if (s != null) {
            // возможно пустой пароль
            bds.setPassword(s);
        }
        s = dbSource.getProps().get(DbSourcePropsConsts.database);
        if (!UtString.empty(s)) {
            bds.setDefaultCatalog(s);
        }

        // connection properties
        Map<String, String> props = dbSource.getProps("conn", false);
        for (String key : props.keySet()) {
            bds.addConnectionProperty(key, props.get(key));
        }

        // init sql
        List<String> sqls = dbSource.getInitConnectionSqls();
        if (sqls != null && sqls.size() > 0) {
            bds.setConnectionInitSqls(sqls);
        }

        return bds;
    }
}
