package jandcode.core.db;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;

import java.sql.*;

/**
 * Тип данных для базы данных.
 */
public interface DbDataType extends INamed {

    /**
     * Значение, которое возвращает метод {@link DbDataType#getValue(java.sql.ResultSet, int)}.
     */
    interface Value {

        /**
         * Значение. Может быть null, но обычно нет.
         */
        Object getValue();

        /**
         * Признак null
         */
        boolean isValueNull();

    }


    /**
     * Прочитать значение из ResultSet.
     * Вызывается только один раз.
     *
     * @param rs        откуда
     * @param columnIdx индекс колонки
     * @return значение
     */
    Value getValue(ResultSet rs, int columnIdx) throws Exception;

    /**
     * Установить значение параметра (не null)
     *
     * @param st       куда
     * @param paramIdx какой параметр
     * @param value    значение (возможен любой тип)
     */
    void setValue(PreparedStatement st, int paramIdx, Object value) throws Exception;

    /**
     * Тип данных (см. {@link VariantDataType})
     */
    VariantDataType getDataType();

    /**
     * Возвращает sql тип для создания поля.
     *
     * @param size размер поля, указывается для строковых данных
     */
    String getSqlType(long size);

    /**
     * Тип данных в store для представления этого типа данных
     */
    String getStoreDataTypeName();

    /**
     * Возвращает sql значение поля, пригодное для использования в DML.
     *
     * @param value значение
     */
    String getSqlValue(Object value);


}
