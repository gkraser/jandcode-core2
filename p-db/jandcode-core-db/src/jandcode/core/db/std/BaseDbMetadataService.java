package jandcode.core.db.std;

import jandcode.commons.*;
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

    /**
     * Нормализация регистра символов в имени талиц/полей
     */
    protected String normalizeCase(String name) {
        return name.toUpperCase();
    }

    /**
     * Создает объект {@link DbMetadataTable} по данным ResultSet.
     * Если строка не таблица, возвращет null.
     */
    protected DbMetadataTable createTable(ResultSet rs) throws Exception {
        String typ = rs.getString("TABLE_TYPE");
        String nm = rs.getString("TABLE_NAME");
        if (nm.indexOf("$") != -1) {
            return null;
        }
        boolean view = "VIEW".equals(typ);
        if ("TABLE".equals(typ) || view || "BASE TABLE".equals(typ)) {
            DbMetadataTable t = new DbMetadataTableImpl(nm, view);
            return t;
        }
        return null;
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
                    DbMetadataTable t = createTable(rs);
                    if (t != null) {
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

    public boolean hasTables() throws Exception {
        boolean res = false;
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
                    DbMetadataTable t = createTable(rs);
                    if (t != null) {
                        res = true;
                        break;
                    }
                }
            } finally {
                rs.close();
            }
        } finally {
            db.disconnect();
        }
        return res;
    }


    public boolean hasTable(String name) throws Exception {
        if (UtString.empty(name)) {
            return false;
        }
        boolean res = false;

        Db db = getDbSource().createDb(true);
        db.connect();
        try {

            String sh = getCurrentSchema(db);
            String ct = getCurrentCatalog(db);

            DatabaseMetaData md = db.getConnection().getMetaData();

            // все таблицы
            ResultSet rs = md.getTables(ct, sh, normalizeCase(name), null);

            try {
                while (rs.next()) {
                    DbMetadataTable t = createTable(rs);
                    if (t != null) {
                        res = true;
                        break;
                    }
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
