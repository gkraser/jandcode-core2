package jandcode.core.dao;

/**
 * Инициализатор DaoContext.
 */
public interface DaoContextIniter {

    /**
     * Проинициализировать контекст dao перед выполнением dao
     *
     * @param ctx контекст для инициализации
     */
    void initDaoContext(DaoContext ctx);

}
