package jandcode.core.store;

import jandcode.commons.named.*;
import jandcode.core.*;

import java.util.*;

/**
 * Хранилище записей данных. Представляет собой список записей.
 * Используется как таблица с данными.
 */
public interface Store extends INamed, INamedSet, Iterable<StoreRecord>,
        IAppLink, ICustomProp,
        IStoreFieldHolder, IStoreFieldBuilder {

    /**
     * Количество записей
     */
    int size();

    /**
     * Получить запись по индексу
     */
    StoreRecord get(int index);

    /**
     * Все записи из store в виде списка. Это ссылка на внутренний список записей,
     * так что манипуляции с ним отражаются на самом store. Например его можно
     * отсортировать.
     */
    List<StoreRecord> getRecords();

    //////

    /**
     * Добавить пустую запись
     */
    StoreRecord add();

    /**
     * Добавить запись, полученную из map
     */
    StoreRecord add(Map values);

    /**
     * Добавить новую запись и инициализировать ее из записи rec
     */
    StoreRecord add(StoreRecord rec);

    //////

    /**
     * Удалить все записи
     */
    void clear();

    /**
     * Удалить запись по индексу
     */
    void remove(int index);

    //////

    /**
     * Обработчик для получения значений из словаря
     */
    IStoreDictResolver getDictResolver();

    void setDictResolver(IStoreDictResolver dictResolver);

    //////

    /**
     * Клонировать структуру store.
     *
     * @return новый экземпляр store с идентичной структурой без данных
     */
    Store cloneStore();

}
