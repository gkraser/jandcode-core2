package jandcode.core.store;

import jandcode.commons.variant.*;

/**
 * Хранилище полей
 */
public interface IStoreFieldHolder extends IVariantFieldsOwner {

    /**
     * Поля store
     */
    Iterable<StoreField> getFields();

    /**
     * Найти поле по имени. Возвращает null, если нет такого поля
     */
    StoreField findField(String fieldName);

    /**
     * Найти поле по имени. Возвращает ошибку, если нет такого поля
     */
    StoreField getField(String fieldName);

    /**
     * Найти поле по индексу. Возвращает ошибку, если нет такого поля
     */
    StoreField getField(int index);

    /**
     * Сколько полей
     */
    int getCountFields();

}
