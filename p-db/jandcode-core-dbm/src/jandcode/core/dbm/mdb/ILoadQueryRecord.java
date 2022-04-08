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
     * Если записей более чем одна, генерируется ошибка.
     * Если записей нет, возвращается null при required=false или
     * генерируется ошибка при required=true
     */
    StoreRecord loadQueryRecord(CharSequence sql, boolean required) throws Exception;

    /**
     * Загрузить одну запись.
     * Если записей нет или более чем одна, генерируется ошибка.
     */
    StoreRecord loadQueryRecord(CharSequence sql, Object params) throws Exception;

    /**
     * Загрузить одну запись.
     * Если записей более чем одна, генерируется ошибка.
     * Если записей нет, возвращается null при required=false или
     * генерируется ошибка при required=true
     */
    StoreRecord loadQueryRecord(CharSequence sql, Object params, boolean required) throws Exception;

    /**
     * Загрузить одну запись в rec. Перед загрузкой - очищается.
     * Если записей нет или более чем одна, генерируется ошибка.
     */
    StoreRecord loadQueryRecord(StoreRecord rec, CharSequence sql) throws Exception;

    /**
     * Загрузить одну запись в rec. Перед загрузкой - очищается.
     * Если записей нет или более чем одна, генерируется ошибка.
     */
    StoreRecord loadQueryRecord(StoreRecord rec, CharSequence sql, Object params) throws Exception;

}
