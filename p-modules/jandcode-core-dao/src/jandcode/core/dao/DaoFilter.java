package jandcode.core.dao;

/**
 * Фильтр для процесса исполнения dao
 */
public interface DaoFilter {

    /**
     * Вызывается перед выполнением dao
     */
    void beforeInvoke(DaoFilterParams p);

    /**
     * Вызывается после выполнения dao
     */
    void afterInvoke(DaoFilterParams p);

    /**
     * Вызывается при ошибке выполнения
     */
    void errorInvoke(DaoFilterParams p);

}
