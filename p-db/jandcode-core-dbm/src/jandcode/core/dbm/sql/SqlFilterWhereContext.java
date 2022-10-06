package jandcode.core.dbm.sql;

import jandcode.commons.variant.*;

public interface SqlFilterWhereContext extends MapFilterValue, ISqlFilterWhere {

    /**
     * Для какого текста sql. Именно он модифицируется
     */
    SqlText getSql();

    /**
     * Для какого фильтра
     */
    SqlFilter getSqlFilter();

    /**
     * Для какого where
     */
    SqlFilterWhere getSqlFilterWhere();

    /**
     * Параметры, собранные для формируемого sql
     */
    IVariantMap getParams();

    /**
     * Добавить условие where
     */
    void addWhere(String where);

    /**
     * Установить значение для параметра
     *
     * @param name  полное имя параметра. Можно получить с помошью paramName
     * @param value значение
     */
    void setParam(String name, Object value);

}
