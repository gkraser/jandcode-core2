package jandcode.core.dbm.mdb;

import jandcode.core.store.*;

/**
 * Загрузка одной записи
 */
public interface ILoadQueryRecord {

    /**
     * Загрузить одну запись.
     * Если записей нет или более чем одна, генерируется ошибка.
     */
    StoreRecord loadQueryRecord(String sql) throws Exception;

    /**
     * Загрузить одну запись.
     * Если записей нет или более чем одна, генерируется ошибка.
     */
    StoreRecord loadQueryRecord(String sql, Object params) throws Exception;

}
