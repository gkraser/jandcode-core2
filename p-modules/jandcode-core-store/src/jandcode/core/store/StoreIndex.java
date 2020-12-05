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
     * Получить запись по ключу.
     *
     * @param key     ключ
     * @param autoAdd при значении true - если записи нет, то она добавляется
     * @return null, если не найдено
     */
    StoreRecord get(Object key, boolean autoAdd);

    /**
     * Переиндексировать
     */
    void reindex();

}
