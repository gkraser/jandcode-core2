package jandcode.db;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;

/**
 * sql тип данных
 */
public interface DbSqlType extends INamed {

    /**
     * Типа данных для базы данных
     */
    DbDataType getDbDataType();

    /**
     * Получить sql тип
     *
     * @param vars переменные подстановки
     */
    String getSqlType(IVariantNamed vars);


}
