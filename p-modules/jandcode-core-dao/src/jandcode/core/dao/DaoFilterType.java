package jandcode.core.dao;

/**
 * Типы фильтров, которые вызываются в процессе обработки dao.
 */
public enum DaoFilterType {

    /**
     * Вызывается перед выполнением dao
     */
    before,

    /**
     * Вызывается после выполнения dao
     */
    after,

    /**
     * Вызывается при ошибке выполнения
     */
    error,

}
