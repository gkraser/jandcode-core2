package jandcode.db;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;

import java.sql.*;

/**
 * Тип данных для базы данных.
 */
public interface DbDataType extends INamed {

    /**
     * Прочитать значение из ResultSet.
     * Вызывается только один раз.
     *
     * @param rs        откуда
     * @param columnIdx индекс колонки
     * @return значение
     */
    Object getValue(ResultSet rs, int columnIdx) throws Exception;

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

}
