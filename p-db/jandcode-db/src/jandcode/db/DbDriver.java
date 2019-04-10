package jandcode.db;

import jandcode.core.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;

import java.sql.*;
import java.util.*;

/**
 * Драйвер базы данных.
 * Создается и существует только в рамках DbSource.
 */
public interface DbDriver extends Comp, IDbSourceMember {

    /**
     * Тип базы данных. Например 'oracle', 'mysql'.
     * Для одного типа базы данных может быть реализованно различные драйвера,
     * однако внешним инструментам нужно знать, что за тип базы используется.
     */
    String getDbType();

    ////// structs

    /**
     * Поддерживаемые типы данных
     */
    NamedList<DbDatatype> getDbDatatypes();

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
    DbDatatype getDbDatatype(ResultSetMetaData md, int colIdx) throws Exception;

    /**
     * Получить тип для колонки с sql-типом
     */
    DbDatatype getDbDatatype(int sqlType) throws Exception;

    //////

    /**
     * Возвращает список sql, которые нужно выполнить при установке соединения.
     * Может возвращает null, если такие sql не требуются.
     * <p>
     * По умолчанию данные собираются из свойств dbsource initConnectionSql.XXX.
     * Сортируются по имени свойства.
     */
    List<String> getInitConnectionSqls();

}
