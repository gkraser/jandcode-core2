package jandcode.core.db;

import jandcode.commons.named.*;

/**
 * Описание поля
 */
public interface DbMetadataField extends INamed {

    /**
     * Тип данных в базе
     */
    DbDataType getDbDataType();

    /**
     * Размер поля
     */
    int getSize();

}
