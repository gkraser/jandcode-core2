package jandcode.core.db;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;

import java.sql.*;

/**
 * Запрос к базе данных.
 */
public interface DbQuery extends IVariantNamed, IVariantIndexed, AutoCloseable {

    /**
     * Поля в запросе
     */
    NamedList<DbQueryField> getFields();

    /**
     * Установить объект в качестве источников значений для параметров.
     * Этот объект должен быть преобразумым в IVariantNamed.
     * Поддерживаемый типы params можно посмотреть
     * в методе {@link UtCnv#toVariantNamed(Object)}.
     * <p>
     * Значения не копируются из params. Соответсвенно, при изменении значений
     * в объекте params, изменения сразу доступны в запросе. Это можно использовать
     * для пакетных обработок.
     */
    void setParams(Object params);

    /**
     * Установить sql запрос.
     */
    void setSql(String sql);

    //////

    /**
     * Выполнить запрос DML. Данные не возвращаются.
     */
    void exec() throws Exception;

    /**
     * Выполнить нативный запрос DML.
     * Параметры не используются, sql никак необрабатывается.
     * Данные не возвращаются.
     */
    void execNative() throws Exception;

    /**
     * Открыть запрос. Первая запись доступна (автоматически выполняется next()).
     */
    void open() throws Exception;

    /**
     * Открыть запрос. Первая запись доступна (автоматически выполняется next()).
     * Параметры не используются, sql никак необрабатывается.
     */
    void openNative() throws Exception;

    /**
     * Закрыть запрос.
     */
    void close();

    /**
     * Прочитать следующую запись.
     */
    void next() throws Exception;

    /**
     * Проверка на конец данных. Возвращает true, если данных больше нет.
     */
    boolean eof();

    //////

    /**
     * Возвращает внутренний ResultSet для открытого запроса.
     */
    ResultSet getResultSet();

    /**
     * Настраивает DbQuery на использование свежеоткрытого ResultSet
     */
    void bindResultSet(ResultSet resultSet) throws Exception;

}
