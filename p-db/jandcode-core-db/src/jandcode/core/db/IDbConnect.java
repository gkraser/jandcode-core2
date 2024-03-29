package jandcode.core.db;

import java.sql.*;

/**
 * Методы обертки вокруг {@link Connection} для {@link Db}.
 */
public interface IDbConnect extends IDbSourceLink {

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
    DbQuery createQuery(CharSequence sql, Object params);


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
     * Имеется ли активная транзакция
     */
    boolean isTran();


    //////

    /**
     * Некоторые параметры для управления поведением {@link Db} и {@link DbQuery}.
     */
    DbParams getDbParams();

}
