package jandcode.core.dbm.domain;

import jandcode.commons.variant.*;

/**
 * Набор основных свойств {@link Field}.
 */
public interface IField {

    /**
     * Заголовок
     */
    String getTitle();

    /**
     * Краткий заголовок
     */
    String getTitleShort();

    /**
     * Размер для строковых полей
     */
    int getSize();

    /**
     * Тип данных поля.
     *
     * @see VariantDataType
     */
    VariantDataType getDataType();

    /**
     * Тип данных поля для базы данных
     */
    String getDbDataType();

    /**
     * Тип данных поля для store
     */
    String getStoreDataType();

    /**
     * Имя домена, на который ссылается поле или пустая строки, если поле не ссылается
     * на домен.
     */
    String getRef();

    /**
     * Ссылается ли поле на домен.
     */
    boolean hasRef();

    /**
     * Имя словаря, связанного с этим полем.
     */
    String getDict();

    /**
     * Имеет ли поле привязанный словарь.
     */
    boolean hasDict();

}
