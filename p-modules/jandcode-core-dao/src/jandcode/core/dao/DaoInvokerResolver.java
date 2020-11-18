package jandcode.core.dao;

/**
 * Интерфейс для тех, кто может по имени daoInvoker предоставить
 * экземпляр DaoInvoker
 */
public interface DaoInvokerResolver {

    /**
     * Получить daoInvoker по имени.
     *
     * @param name имя daoInvoker
     * @return null, если daoInvoker здесь не найден
     */
    DaoInvoker resolveDaoInvoker(String name);


}
