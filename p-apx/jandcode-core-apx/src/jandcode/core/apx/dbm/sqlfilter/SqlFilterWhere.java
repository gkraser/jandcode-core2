package jandcode.core.apx.dbm.sqlfilter;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;

/**
 * Элемент фильтра.
 */
public interface SqlFilterWhere extends INamed {

    /**
     * Построитель where
     */
    SqlFilterBuilder getBuilder();

    /**
     * Произвольные атрибуты
     */
    IVariantMap getAttrs();

    /**
     * Добавить еще один builder
     *
     * @return возвращает себя
     */
    SqlFilterWhere addBuilder(SqlFilterBuilder builder);

}
