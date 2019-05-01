package jandcode.db;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;

import java.sql.*;
import java.util.*;

/**
 * Драйвер базы данных.
 */
public interface DbDriver extends INamed, IConfLink {

    /**
     * Тип базы данных. Например 'oracle', 'mysql'.
     * Для одного типа базы данных может быть реализованны различные драйвера,
     * однако внешним инструментам нужно знать, что за тип базы используется.
     */
    String getDbType();

    ////// structs

    /**
     * Поддерживаемые типы данных
     */
    NamedList<DbDataType> getDbDataTypes();

    //////

    /**
     * Присвоить параметры запросу.
     *
     * @param statement   куда
     * @param paramValues откуда
     * @param paramNames  имена параметров в порядке появления '?' в sql
     */
    void assignStatementParams(PreparedStatement statement, IVariantNamed paramValues, List<String> paramNames) throws Exception;

    /**
     * Получить тип для колонки ResultSet
     */
    DbDataType getDbDataType(ResultSetMetaData md, int colIdx) throws Exception;

    /**
     * Получить тип для колонки с sql-типом
     */
    DbDataType getDbDataType(int sqlType) throws Exception;

}
