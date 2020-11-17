package jandcode.core.dao;

/**
 * Логгер для dao.
 */
public interface DaoLogger {

    /**
     * Превратить dao в строку лога
     */
    String toString(DaoContext ctx);

    /**
     * Начало выполнения dao
     */
    void logStart(DaoContext ctx);

    /**
     * Конец выполнения dao
     */
    void logStop(DaoContext ctx);

}
