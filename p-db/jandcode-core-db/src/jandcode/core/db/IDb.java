package jandcode.core.db;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Методы {@link Db}.
 */
public interface IDb extends IDbConnect {

    ////// query

    /**
     * Создать запрос с указанием sql
     *
     * @param sql
     */
    DbQuery createQuery(CharSequence sql);

    /**
     * Выполнить запрос без параметров
     *
     * @param sql
     */
    void execQuery(CharSequence sql) throws Exception;

    /**
     * Выполнить запрос c параметрами
     */
    void execQuery(CharSequence sql, Object params) throws Exception;

    /**
     * Открыть запрос без параметров
     *
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
     *
     * @param sql
     */
    void execQueryNative(CharSequence sql) throws Exception;

    /**
     * Открыть нативный запрос без параметров
     *
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
     *
     * @return store
     */
    Store loadQuery(Store store, DbQuery query) throws Exception;

    /**
     * Загрузить query в новый store.
     * query не закрывается (не я открыл, не мне закрывать)!
     */
    Store loadQuery(DbQuery query) throws Exception;

    //////

    /**
     * Загрузить результат запроса в новый store
     *
     * @param sql
     */
    Store loadQuery(CharSequence sql) throws Exception;

    /**
     * Загрузить результат запроса с параметрами в новый store
     */
    Store loadQuery(CharSequence sql, Object params) throws Exception;

    /**
     * Загрузить результат нативного запроса в новый store
     *
     * @param sql
     */
    Store loadQueryNative(CharSequence sql) throws Exception;

    //////

    /**
     * Загрузить результат запроса в указанный store
     *
     * @return store
     */
    Store loadQuery(Store store, CharSequence sql) throws Exception;

    /**
     * Загрузить результат запроса с параметрами в указанный store
     *
     * @return store
     */
    Store loadQuery(Store store, CharSequence sql, Object params) throws Exception;

    /**
     * Загрузить результат нативного запроса в указанный store
     *
     * @return store
     */
    Store loadQueryNative(Store store, CharSequence sql) throws Exception;


    ////// script

    /**
     * Исполнение скрипта. Скрипт - это набор sql операторов.
     *
     * @param script   элементы скрипта
     * @param isNative true - выполняется execQueryNative, иначе execQuery
     * @param onError  обработчик ошибок. Если возвращает false, обработка прекращается.
     *                 Если не задан, то при первой ошибке выполнение прекращется.
     */
    void execScript(List<? extends CharSequence> script, boolean isNative, ErrorCallback onError) throws Exception;

    /**
     * Исполнение скрипта. Скрипт - это набор sql операторов, разделенных {@link UtSql#SCRIPT_DELIMITER}.
     *
     * @param script   скрипт
     * @param isNative true - выполняется execQueryNative, иначе execQuery
     * @param onError  обработчик ошибок. Если возвращает false, обработка прекращается
     *                 Если не задан, то при первой ошибке выполнение прекращется.
     */
    void execScript(CharSequence script, boolean isNative, ErrorCallback onError) throws Exception;

    /**
     * см: {@link IDb#execScript(java.lang.CharSequence, boolean, ErrorCallback)},
     * где isNative=true
     */
    default void execScript(CharSequence script, ErrorCallback onError) throws Exception {
        execScript(script, true, onError);
    }

    /**
     * см: {@link IDb#execScript(java.lang.CharSequence, boolean, ErrorCallback)},
     * где isNative=true, onError=null
     */
    default void execScript(CharSequence script) throws Exception {
        execScript(script, true, null);
    }

}
