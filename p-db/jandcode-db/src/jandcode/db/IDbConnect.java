package jandcode.db;

import java.sql.*;

/**
 * Методы обертки вокруг {@link Connection} для {@link Db}.
 */
public interface IDbConnect {

    ////// connect

    /**
     * Установить соединение.
     * Вызовы connect/disconnect должны быть сбалансированы.
     */
    void connect() throws Exception;

    /**
     * Обрыв соединения
     * Вызовы connect/disconnect должны быть сбалансированы.
     */
    void disconnect() throws Exception;

    /**
     * Обрыв соединения с игнорированием баланса connect/disconnect.
     */
    void disconnectForce() throws Exception;

    /**
     * Текущее jdbc соединение.
     */
    Connection getConnection();

    /**
     * Установленно ли соединение
     */
    boolean isConnected();

    ////// query

    /**
     * Создать запрос с указанием sql и параметров
     */
    DbQuery createQuery(String sql, Object params);

    /**
     * Создать запрос с указанием sql
     */
    DbQuery createQuery(String sql);

    /**
     * Выполнить запрос без параметров
     */
    void execQuery(String sql) throws Exception;

    /**
     * Выполнить запрос c параметрами
     */
    void execQuery(String sql, Object params) throws Exception;

    /**
     * Открыть запрос без параметров
     */
    DbQuery openQuery(String sql) throws Exception;

    /**
     * Открыть запрос c параметрами
     */
    DbQuery openQuery(String sql, Object params) throws Exception;

    ////// query native

    /**
     * Выполнить нативный запрос без параметров
     */
    void execQueryNative(String sql) throws Exception;

    /**
     * Открыть нативный запрос без параметров
     */
    DbQuery openQueryNative(String sql) throws Exception;


    ////// tran

    /**
     * Начать транзакцию.
     * Вызовы startTran/commit должны быть сбалансированы.
     */
    void startTran() throws Exception;

    /**
     * Подтвердить транзакцию.
     * Вызовы startTran/commit должны быть сбалансированы.
     */
    void commit() throws Exception;

    /**
     * Откатить транзакцию.
     * Не зависит от баланса startTran/commit, срабатывает сразу.
     */
    void rollback() throws Exception;

    /**
     * Откатить транзакцию и сгенерировать ошибку
     * Не зависит от баланса startTran/commit, срабатывает сразу.
     */
    void rollback(Exception e) throws Exception;

    /**
     * Имеется ли активная транзакция
     */
    boolean isTran();

}
