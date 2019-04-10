package jandcode.db;

import jandcode.commons.named.*;

/**
 * Описание поля
 */
public interface DbMetadataField extends INamed {

    /**
     * Тип данных в базе
     */
    DbDatatype getDbDatatype();

    /**
     * Размер поля
     */
    int getSize();

}
