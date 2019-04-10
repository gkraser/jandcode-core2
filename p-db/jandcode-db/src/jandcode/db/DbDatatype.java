package jandcode.db;

import jandcode.core.*;
import jandcode.commons.variant.*;

import java.sql.*;

/**
 * Тип данных для базы данных.
 */
public interface DbDatatype extends Comp, IDbSourceMember {

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
    VariantDataType getDatatype();

    /**
     * Возвращает sql тип для создания поля. В типе может использоватся '${size}', который
     * заменяется на размер.
     */
    String getSqlType();

    /**
     * Возвращает sql тип для создания поля.
     *
     * @param size размер поля, указывается для строковых данных
     */
    String getSqlType(long size);

}
