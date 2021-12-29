package jandcode.core.dbm.domain;

import jandcode.commons.variant.*;
import jandcode.core.db.*;
import jandcode.core.store.*;

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
    DbDataType getDbDataType();

    /**
     * Тип данных поля для store
     */
    StoreDataType getStoreDataType();

    /**
     * sql тип поля в виде строки
     */
    String getSqlType();

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
     * Если поле ссылка, то она каскадная.
     * Это означает, что при удалении записи, на которую идет ссылка,
     * должна быть удалена и запись, откуда идет ссылка.
     */
    boolean isRefCascade();

    /**
     * Имя словаря, связанного с этим полем.
     */
    String getDict();

    /**
     * Имеет ли поле привязанный словарь.
     */
    boolean hasDict();

}
