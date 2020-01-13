package jandcode.core.dao;

/**
 * Логгер для dao.
 */
public interface DaoLogger {

    /**
     * Превратить dao в строку лога
     */
    String toString(DaoFilterParams p);

    /**
     * Начало выполнения dao
     */
    void logStart(DaoFilterParams p);

    /**
     * Конец выполнения dao
     */
    void logStop(DaoFilterParams p);

}
