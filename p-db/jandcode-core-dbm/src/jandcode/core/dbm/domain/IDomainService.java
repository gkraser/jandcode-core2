package jandcode.core.dbm.domain;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Методы сервиса доменов
 */
public interface IDomainService extends IDomainHolder {

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
     * Создать построитель динамического домена.
     *
     * @param parentDomain предок домена
     */
    DomainBuilder createDomainBuilder(String parentDomain);

    /**
     * Создать экземпляр домена по conf.
     *
     * @param x    конфигурация домена, не раскрытая, будет раскрыта перед созданием
     * @param name имя создаваемого домена
     */
    Domain createDomain(Conf x, String name);

    /**
     * Создать экземпляр домена по структуре store.
     *
     * @param store для какого store
     */
    Domain createDomain(Store store);

    /**
     * Создать пустой store со структурой как в домене.
     *
     * @param domain для какого домена
     */
    Store createStore(Domain domain);

    /**
     * Создать пустой store со структурой как в домене.
     *
     * @param domainName для какого домена
     */
    default Store createStore(String domainName) {
        return createStore(getDomain(domainName));
    }

    /**
     * Создать пустую StoreRecord со структурой как в домене.
     *
     * @param domain для какого домена
     */
    StoreRecord createStoreRecord(Domain domain);

    /**
     * Создать пустой store со структурой как в домене.
     *
     * @param domainName для какого домена
     */
    default StoreRecord createStoreRecord(String domainName) {
        return createStoreRecord(getDomain(domainName));
    }

    /**
     * Создать StoreRecord со структурой как в домене и с данными из data
     *
     * @param domain для какого домена
     * @param data   данные для записи
     */
    StoreRecord createStoreRecord(Domain domain, StoreRecord data);

    /**
     * Создать StoreRecord со структурой как в домене и с данными из data
     *
     * @param domainName для какого домена
     * @param data       данные для записи
     */
    default StoreRecord createStoreRecord(String domainName, StoreRecord data) {
        return createStoreRecord(getDomain(domainName), data);
    }

    /**
     * Создать StoreRecord со структурой как в домене и с данными из data
     *
     * @param domain для какого домена
     * @param data   данные для записи
     */
    StoreRecord createStoreRecord(Domain domain, Map data);

    /**
     * Создать StoreRecord со структурой как в домене и с данными из data
     *
     * @param domainName для какого домена
     * @param data       данные для записи
     */
    default StoreRecord createStoreRecord(String domainName, Map data) {
        return createStoreRecord(getDomain(domainName), data);
    }

}
