package jandcode.core.apx.dbm.sqlfilter;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.dbm.sql.*;

public interface SqlFilterContext extends INamed, MapFilterValue {

    /**
     * Для какого текста sql. Именно он модифицируется
     */
    SqlText getSql();

    /**
     * Для какого фильтра
     */
    SqlFilter getSqlFilter();

    /**
     * Произвольные атрибуты
     */
    IVariantMap getAttrs();

    /**
     * Ключ в параметрах, откуда будут браться значения для where.
     * Соотвествует имени, если явно не назначен.
     * <p>
     * attrs: key
     */
    String getKey();

    /**
     * Место, в которое вставлять условие. Имя поименнованного where в sql-тексте.
     * Если не указано = default.
     * <p>
     * attrs: wherePlace
     */
    String getWherePlace();

    /**
     * Для какого поля в sql предназначено. По умолчанию - имя.
     * <p>
     * attrs: sqlField
     */
    String getSqlField();

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

    //////

    /**
     * Добавить часть sql
     */
    void addPart(String partName, String partText);

}
