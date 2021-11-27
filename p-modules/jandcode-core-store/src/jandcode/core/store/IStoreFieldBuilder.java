package jandcode.core.store;

/**
 * Интерфейс построения полей store
 */
public interface IStoreFieldBuilder {

    /**
     * Добавить поле
     *
     * @param name  имя поля
     * @param field экземпляр поля (новый)
     * @return добавленный экземпляр поля
     */
    StoreField addField(String name, StoreField field);

    /**
     * Добавить поле
     *
     * @param name имя поля
     * @param type тип поля
     * @return добавленный экземпляр поля
     */
    StoreField addField(String name, String type);

    /**
     * Добавить поле
     *
     * @param name имя поля
     * @param type тип поля
     * @param size размер поля
     * @return добавленный экземпляр поля
     */
    StoreField addField(String name, String type, int size);

    /**
     * Добавить поле
     *
     * @param name      имя поля
     * @param valueType тип значения поля
     * @return добавленный экземпляр поля
     */
    StoreField addField(String name, Class valueType);

    /**
     * Добавить поле
     *
     * @param name      имя поля
     * @param valueType тип значения поля
     * @param size      размер поля
     * @return добавленный экземпляр поля
     */
    StoreField addField(String name, Class valueType, int size);

    /**
     * Удалить поле
     */
    void removeField(String name);

}
