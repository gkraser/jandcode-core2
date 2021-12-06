package jandcode.core.db;

import jandcode.core.store.*;

/**
 * Методы-утилиты для {@link Db}.
 */
public interface IDbUtils {

    ////// query

    /**
     * Создать запрос с указанием sql
     * @param sql
     */
    DbQuery createQuery(CharSequence sql);

    /**
     * Выполнить запрос без параметров
     * @param sql
     */
    void execQuery(CharSequence sql) throws Exception;

    /**
     * Выполнить запрос c параметрами
     */
    void execQuery(CharSequence sql, Object params) throws Exception;

    /**
     * Открыть запрос без параметров
     * @param sql
     */
    DbQuery openQuery(CharSequence sql) throws Exception;

    /**
     * Открыть запрос c параметрами
     */
    DbQuery openQuery(CharSequence sql, Object params) throws Exception;


    ////// query native

    /**
     * Выполнить нативный запрос без параметров
     * @param sql
     */
    void execQueryNative(CharSequence sql) throws Exception;

    /**
     * Открыть нативный запрос без параметров
     * @param sql
     */
    DbQuery openQueryNative(CharSequence sql) throws Exception;


    ////// tran

    /**
     * Откатить транзакцию и сгенерировать ошибку
     * Не зависит от баланса startTran/commit, срабатывает сразу.
     */
    void rollback(Exception e) throws Exception;


    ////// store

    /**
     * Создать пустой store
     */
    Store createStore();

    /**
     * Создать пустой store со структурой из query.
     */
    Store createStore(DbQuery query);

    /**
     * Загрузить данные из query в указанный store.
     * query не закрывается (не я открыл, не мне закрывать)!
     */
    void loadQuery(Store store, DbQuery query) throws Exception;

    /**
     * Загрузить query в новый store.
     * query не закрывается (не я открыл, не мне закрывать)!
     */
    Store loadQuery(DbQuery query) throws Exception;

    //////

    /**
     * Загрузить результат запроса в новый store
     * @param sql
     */
    Store loadQuery(CharSequence sql) throws Exception;

    /**
     * Загрузить результат запроса с параметрами в новый store
     */
    Store loadQuery(CharSequence sql, Object params) throws Exception;

    /**
     * Загрузить результат нативного запроса в новый store
     * @param sql
     */
    Store loadQueryNative(CharSequence sql) throws Exception;

    //////

    /**
     * Загрузить результат запроса в указанный store
     */
    void loadQuery(Store store, CharSequence sql) throws Exception;

    /**
     * Загрузить результат запроса с параметрами в указанный store
     */
    void loadQuery(Store store, CharSequence sql, Object params) throws Exception;

    /**
     * Загрузить результат нативного запроса в указанный store
     */
    void loadQueryNative(Store store, CharSequence sql) throws Exception;


    //////


}
