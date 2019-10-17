package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.db.*;

import java.sql.*;
import java.util.*;

/**
 * Базовый предок для {@link DbConnectionService}.
 */
public abstract class BaseDbConnectionService extends BaseDbSourceMember implements DbConnectionService {

    private List<String> initConnectionSqls;

    /**
     * Возвращает список sql, которые нужно выполнить при установке соединения.
     * Может возвращает null, если такие sql не требуются.
     * <p>
     * Данные собираются из свойств dbsource initConnectionSql.XXX.
     * Сортируются по имени свойства.
     */
    public List<String> getInitConnectionSqls() {
        if (initConnectionSqls == null) {
            synchronized (this) {
                if (initConnectionSqls == null) {
                    initConnectionSqls = grabInitConnectionSqls();
                }
            }
        }
        return initConnectionSqls;
    }

    protected List<String> grabInitConnectionSqls() {
        List<String> res = new ArrayList<>();
        IVariantMap p = getDbSource().getProps().subMap(DbSourcePropsConsts.initConnectionSql);
        TreeMap<String, Object> m = new TreeMap<>(p);
        for (Object s : m.values()) {
            res.add(UtCnv.toString(s));
        }
        return res;
    }

    //////

    public void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new XErrorWrap(e);
            }
        }
    }


}
