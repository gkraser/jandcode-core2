package jandcode.core.db;

/**
 * Методы-утилиты для {@link Db}.
 */
public interface IDbUtils {

    ////// query

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
     * Откатить транзакцию и сгенерировать ошибку
     * Не зависит от баланса startTran/commit, срабатывает сразу.
     */
    void rollback(Exception e) throws Exception;


}
