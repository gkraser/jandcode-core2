package jandcode.core.dbm.sql;

import jandcode.commons.variant.*;
import jandcode.core.dbm.*;

import java.util.*;

/**
 * Класс для формирования sql-запросов с переменной частью where, orderBy и пагинацией.
 */
public interface SqlFilter extends IModelLink {

    /**
     * Оригинальный набор параметров, которые переданы экземпляру при создании
     */
    Map<String, Object> getOrigParams();

    /**
     * Оригинальный sql, который передан экземпляру при создании
     */
    String getOrigSql();

    //////

    /**
     * Параметры, собранные для формируемого sql
     */
    IVariantMap getParams();

    /**
     * sql, для получения списка записей
     */
    SqlText getSql();

    /**
     * sql, для получения общего количества записей. Используется при пагинации.
     */
    SqlText getSqlCount();

    //////

    /**
     * Добавить where
     *
     * @param name    имя
     * @param builder построитель
     */
    SqlFilterWhere addWhere(String name, SqlFilterWhereBuilder builder);

    /**
     * Добавить where
     *
     * @param name               имя
     * @param sqlFilterWhereName имя зарегистрованного {@link SqlFilterWhere}
     */
    SqlFilterWhere addWhere(String name, String sqlFilterWhereName);

    /**
     * Добавить where
     *
     * @param name               имя
     * @param sqlFilterWhereName имя зарегистрованного {@link SqlFilterWhere}
     * @param attrs              произвольные атрибуты. Если совпадают с именами свойст
     *                           экземпляра {@link SqlFilterWhere}, то будут присвоены соотвествующим
     *                           свойствамы
     */
    SqlFilterWhere addWhere(String name, String sqlFilterWhereName, Map attrs);

}
