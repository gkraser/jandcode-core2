package jandcode.core.dbm.mdb;

import jandcode.core.store.*;

import java.util.*;

/**
 * Методы {@link Mdb} для работы с записями в базе данных
 */
public interface IMdbRec {

    /**
     * Вставка записи в таблицу в базе данных.
     * id генерится автоматически, если не присутствует в params
     * или при generateId=true
     *
     * @param tableName  имя таблицы
     * @param params     данные и поля
     * @param generateId true - генерить id безусловно, false - если id не установлена, генерить
     * @return id записи
     */
    long insertRec(String tableName, Map params, boolean generateId) throws Exception;

    /**
     * Вставка записи в таблицу в базе данных.
     * id генерится автоматически, если не присутствует в params.
     *
     * @param tableName имя таблицы
     * @param params    данные и поля
     * @return id записи
     */
    default long insertRec(String tableName, Map params) throws Exception {
        return insertRec(tableName, params, false);
    }

    /**
     * Вставка записи в таблицу в базе данных.
     * id генерится автоматически, если не присутствует в params
     * или при generateId=true
     *
     * @param tableName  имя таблицы
     * @param params     данные и поля
     * @param generateId true - генерить id безусловно, false - если id не установлена, генерить
     * @return id записи
     */
    long insertRec(String tableName, StoreRecord params, boolean generateId) throws Exception;

    /**
     * Вставка записи в таблицу в базе данных.
     * id генерится автоматически, если не присутствует в params.
     *
     * @param tableName имя таблицы
     * @param params    данные и поля
     * @return id записи
     */
    default long insertRec(String tableName, StoreRecord params) throws Exception {
        return insertRec(tableName, params, false);
    }

    /**
     * Обновление записи в таблице в базе данных по id.
     * id должен присутствовать в params.
     *
     * @param tableName имя таблицы
     * @param params    данные и поля
     */
    void updateRec(String tableName, Map params) throws Exception;

    /**
     * Обновление записи в таблице в базе данных по id.
     * id должен присутствовать в params.
     *
     * @param tableName имя таблицы
     * @param params    данные и поля
     */
    void updateRec(String tableName, StoreRecord params) throws Exception;

    /**
     * Удаление записи в таблице в базе данных.
     * params не должен быть пустым. Все перечисленные поля используются как
     * условие where для оператора delete.
     *
     * @param tableName имя таблицы
     * @param params    данные и поля
     */
    void deleteRec(String tableName, Map params) throws Exception;

    /**
     * Удаление записи в таблице в базе данных по id.
     *
     * @param tableName имя таблицы
     * @param id        id удаляемой записи
     */
    void deleteRec(String tableName, long id) throws Exception;


}
