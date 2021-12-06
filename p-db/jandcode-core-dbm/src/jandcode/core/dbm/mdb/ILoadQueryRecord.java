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
    StoreRecord loadQueryRecord(CharSequence sql) throws Exception;

    /**
     * Загрузить одну запись.
     * Если записей нет или более чем одна, генерируется ошибка.
     */
    StoreRecord loadQueryRecord(CharSequence sql, Object params) throws Exception;

    /**
     * Загрузить одну запись в rec. Перед загрузкой - очищается.
     * Если записей нет или более чем одна, генерируется ошибка.
     *
     * @return rec
     */
    StoreRecord loadQueryRecord(StoreRecord rec, CharSequence sql) throws Exception;

    /**
     * Загрузить одну запись в rec. Перед загрузкой - очищается.
     * Если записей нет или более чем одна, генерируется ошибка.
     *
     * @return rec
     */
    StoreRecord loadQueryRecord(StoreRecord rec, CharSequence sql, Object params) throws Exception;

}
