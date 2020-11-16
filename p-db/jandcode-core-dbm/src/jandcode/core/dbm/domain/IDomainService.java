package jandcode.core.dbm.domain;

import jandcode.commons.named.*;

/**
 * Методы сервиса доменов
 */
public interface IDomainService {

    /**
     * Домены. Кешированный список.
     */
    NamedList<Domain> getDomains();

    /**
     * Получить домен по имени.
     *
     * @return error, если не найдено
     */
    default Domain getDomain(String name) {
        return getDomains().get(name);
    }

    /**
     * Найти домен по имени
     *
     * @return null, если не найдено
     */
    default Domain findDomain(String name) {
        return getDomains().find(name);
    }

    /**
     * Получить домен по имени
     *
     * @return error, если не найдено
     */
    default Domain domain(String name) {
        return getDomains().get(name);
    }

}
