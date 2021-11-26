package jandcode.core.store;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Интерфейс записи с нетипизированными данными.
 * Запись всегда принадлежит какому-либо store.
 */
public interface StoreRecord extends
        IValueNamed, IValueNamedSet,
        IValueIndexed, IValueIndexedSet,
        IVariantNamed, IVariantNamedDefault, IVariantIndexed,
        IStoreFieldHolder {


    /**
     * Какому store принадлежит
     */
    Store getStore();

    //////

    /**
     * Проверка на null значения по индексу поля
     */
    boolean isValueNull(int index);

    //////

    /**
     * Значения всех полей, которые не null, в виде map
     */
    Map<String, Object> getValues();

    /**
     * Установить значения для полей. Если ключ есть в values,
     * но такого поля не существует,
     * то он игнорируется.
     */
    void setValues(Map values);

    /**
     * Установить значения для полей.
     * Если поле есть в rec, но такого поля не существует,
     * то оно игнорируется.
     */
    void setValues(StoreRecord rec);

    /**
     * Установить значения для полей из getters указанного объекта.
     * Если значение есть в getters, но такого поля не существует,
     * то оно игнорируется.
     */
    void setValues(Object inst);


    ////// для groovy

    /**
     * Синоним для {@link StoreRecord#getValue(String)}
     */
    Object get(String fieldName);

    /**
     * Синоним для {@link StoreRecord#setValue(String, Object)}
     */
    void set(String fieldName, Object value);

    //////

    /**
     * Очистить значения для всех полей.
     */
    void clear();


    ////// dict

    /**
     * Значение из словаря, если поле словарное
     *
     * @param fieldName имя поля с данными
     * @param dictField имя словарного поля. null для поля словаря по умолчанию
     * @return текст из словаря. Пустая строка, если нет значения.
     */
    String getDictText(String fieldName, String dictField);

    /**
     * Значение из словаря, если поле словарное. Используется поле словаря по умолчанию
     *
     * @param fieldName имя поля с данными
     * @return текст из словаря. Пустая строка, если нет значения.
     */
    String getDictText(String fieldName);

    /**
     * Значение из словаря, если поле словарное
     *
     * @param fieldName имя поля с данными
     * @param dictField имя словарного поля. null для поля словаря по умолчанию
     * @return значение из словаря. null, если нет значения.
     */
    Object getDictValue(String fieldName, String dictField);

    /**
     * Значение из словаря, если поле словарное. Используется поле словаря по умолчанию
     *
     * @param fieldName имя поля с данными
     * @return значение из словаря. null, если нет значения.
     */
    Object getDictValue(String fieldName);


}
