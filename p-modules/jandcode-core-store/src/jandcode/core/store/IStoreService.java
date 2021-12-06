package jandcode.core.store;

import jandcode.commons.reflect.*;

/**
 * Методы сервиса store
 */
public interface IStoreService {

    /**
     * Создать экземпляр поля по имени типа
     */
    StoreField createStoreField(String type);

    /**
     * Создать экземпляр поля по типу значения
     */
    StoreField createStoreField(Class valueType);

    /**
     * Создать пустой store без полей
     */
    Store createStore();

    /**
     * Создать store с полями, как у класса. Учитываются аннотации {@link FieldProps}
     * для полей и getter.
     *
     * @see ReflectRecord
     */
    Store createStore(Class cls);

}
