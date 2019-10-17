package jandcode.core.store;

import jandcode.commons.named.*;

/**
 * Поле для {@link Store}.
 */
public interface StoreField extends INamed {

    /**
     * Тип данных для поля
     */
    StoreDataType getStoreDataType();

    /**
     * Индекс поля
     */
    int getIndex();

    /**
     * Размер поля (для строковых полей).
     */
    int getSize();

    void setSize(int size);

    /**
     * Имя словаря, связанного с этим полем.
     */
    String getDict();

    void setDict(String dict);


}
