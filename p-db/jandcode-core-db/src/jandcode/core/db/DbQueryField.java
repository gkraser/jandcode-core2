package jandcode.core.db;

import jandcode.commons.named.*;

/**
 * Поле в DbQuery
 */
public interface DbQueryField extends INamed {

    /**
     * Порядковый номер поля в запросе
     */
    int getIndex();

    /**
     * Типа данных поля для базы данных
     */
    DbDataType getDbDataType();

    /**
     * Оригинальное имя поля, которое пришло в ответе
     */
    String getNameOrig();

}
