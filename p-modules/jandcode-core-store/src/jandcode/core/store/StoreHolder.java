package jandcode.core.store;

import jandcode.commons.named.*;
import jandcode.core.store.impl.*;

/**
 * Хранилище поименнованных store.
 * <p>
 * store, которые попали в это хранилище получают имена, под которыми и хранятся.
 */
public interface StoreHolder extends Iterable<Store> {

    /**
     * Создать экземпляр StoreHolder
     */
    static StoreHolder create() {
        return new StoreHolderImpl();
    }

    /**
     * Список store в хранилище
     */
    NamedList<Store> getItems();

    /**
     * Добавить store, должно иметь имя
     */
    void add(Store store);

    /**
     * Добавить store с указанным именем.
     * Объект store приваивается указанное имя.
     */
    void add(String name, Store store);

    /**
     * Найти store по имени
     *
     * @param name имя store
     * @return null, если store с указанным именем нет
     */
    Store find(String name);

}
