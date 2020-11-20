package jandcode.core.store;

/**
 * Индекс по полю для store
 */
public interface StoreIndex {

    /**
     * Для какого store
     */
    Store getStore();

    /**
     * Для какого поля
     */
    StoreField getField();

    /**
     * Получить запись по ключу
     *
     * @param key ключ
     * @return null, если не найдено
     */
    StoreRecord get(Object key);

    /**
     * Переиндексировать
     */
    void reindex();

}
