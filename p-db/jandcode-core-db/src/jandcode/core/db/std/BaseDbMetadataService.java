package jandcode.core.db.std;

import jandcode.commons.named.*;
import jandcode.core.db.*;
import jandcode.core.db.impl.*;

import java.sql.*;

/**
 * Базовая реализация DbMetadataService и предок для специализированных.
 */
public class BaseDbMetadataService extends BaseDbSourceMember implements DbMetadataService {

    /**
     * Текущая схема.
     * Для переопределения в потомках.
     *
     * @param db база данных с установленным соединением
     */
    protected String getCurrentSchema(Db db) throws Exception {
        return db.getConnection().getSchema();
    }

    /**
     * Текущий каталог.
     * Для переопределения в потомках.
     *
     * @param db база данных с установленным соединением
     */
    protected String getCurrentCatalog(Db db) throws Exception {
        return db.getConnection().getCatalog();
    }

    public NamedList<DbMetadataTable> loadTables() throws Exception {
        NamedList<DbMetadataTable> res = new DefaultNamedList<>();

        Db db = getDbSource().createDb(true);
        db.connect();
        try {

            String sh = getCurrentSchema(db);
            String ct = getCurrentCatalog(db);

            DatabaseMetaData md = db.getConnection().getMetaData();

            // все таблицы
            ResultSet rs = md.getTables(ct, sh, "%", null);

            try {
                while (rs.next()) {
                    String typ = rs.getString("TABLE_TYPE");
                    String nm = rs.getString("TABLE_NAME");
                    if (nm.indexOf("$") != -1) {
                        continue;
                    }
                    if ("TABLE".equals(typ) || "VIEW".equals(typ) || "BASE TABLE".equals(typ)) {
                        DbMetadataTable t = new DbMetadataTableImpl(nm);
                        res.add(t);
                    }
                }
            } finally {
                rs.close();
            }

            // все колонки
            rs = md.getColumns(ct, sh, "%", "%");
            try {
                while (rs.next()) {
                    DbMetadataTableImpl t = (DbMetadataTableImpl) res.find(rs.getString("TABLE_NAME"));
                    if (t == null) {
                        continue;
                    }
                    String nm = rs.getString("COLUMN_NAME");
                    DbDataType dt = getDbSource().getDbDriver().getDbDataType(rs.getInt("DATA_TYPE"));
                    int sz = rs.getInt("COLUMN_SIZE");
                    t.addField(nm, dt, sz);
                }
            } finally {
                rs.close();
            }
        } finally {
            db.disconnect();
        }
        return res;

    }

}
