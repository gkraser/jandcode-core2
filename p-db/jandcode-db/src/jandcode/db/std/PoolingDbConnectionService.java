package jandcode.db.std;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.db.*;
import org.apache.commons.dbcp2.*;

import java.sql.*;
import java.util.*;

/**
 * Настройка {@link BasicDataSource} производится через свойства вида:
 * dbcp.XXXX, где XXX можно посмотреть тут:
 * https://commons.apache.org/proper/commons-dbcp/configuration.html
 */
public class PoolingDbConnectionService extends BaseDbConnectionService {

    protected BasicDataSource datasource;

    ////

    public Connection connect() {
        try {
            return getDatasource().getConnection();
        } catch (Exception e) {
            throw new XErrorWrap(e);
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
        IVariantMap dbsProps = dbSource.getProps();

        // создаем datasource с использованием свойств dbcp.XXXX
        Properties dbcpProps = new Properties();
        dbcpProps.putAll(dbSource.getProps().subMap("dbcp"));
        BasicDataSource bds = BasicDataSourceFactory.createDataSource(dbcpProps);

        bds.setUrl(dbsProps.getString(DbSourcePropsConsts.url));
        String s = dbsProps.getString(DbSourcePropsConsts.username, null);
        if (s != null) {
            bds.setUsername(s);
        }
        s = dbsProps.getString(DbSourcePropsConsts.password, null);
        if (s != null) {
            // возможно пустой пароль
            bds.setPassword(s);
        }
        s = dbsProps.getString(DbSourcePropsConsts.database);
        if (!UtString.empty(s)) {
            bds.setDefaultCatalog(s);
        }

        // connection properties
        IVariantMap props = dbSource.getProps().subMap("conn");
        for (String key : props.keySet()) {
            bds.addConnectionProperty(key, props.getString(key));
        }

        // init sql
        List<String> sqls = getInitConnectionSqls();
        if (sqls.size() > 0) {
            bds.setConnectionInitSqls(sqls);
        }

        return bds;
    }
}
