package jandcode.db;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;

/**
 * Поле в DbQuery
 */
public interface DbQueryField extends INamed {

    /**
     * Тип данных поля (см. {@link VariantDataType})
     */
    VariantDataType getDatatype();

    /**
     * Порядковый номер поля в запросе
     */
    int getIndex();

    /**
     * Имя типа данных поля для базы данных
     */
    String getDbDatatypeName();

}
