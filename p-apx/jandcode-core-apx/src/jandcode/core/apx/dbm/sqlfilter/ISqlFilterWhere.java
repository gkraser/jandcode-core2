package jandcode.core.apx.dbm.sqlfilter;

import jandcode.commons.variant.*;

/**
 * Методы SqlFilterWhere, общие для context
 */
public interface ISqlFilterWhere {

    /**
     * Произвольные атрибуты
     */
    IVariantMap getAttrs();

    /**
     * Ключ в параметрах, откуда будут браться значения для where.
     * Соотвествует имени, если явно не назначен.
     */
    String getKey();

    /**
     * Место, в которое вставлять условие. Имя поименнованного where в sql-тексте.
     * Если не указано = default.
     */
    String getWherePlace();

    /**
     * Для какого поля в sql предназначено. По умолчанию - имя.
     */
    String getSqlField();

}
