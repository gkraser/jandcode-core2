package jandcode.core.apx.dbm.sqlfilter;

import jandcode.core.apx.store.*;

/**
 * Константы для SqlFilter
 */
public class SqlFilterConsts {

    /**
     * Имя свойства с информацией о пагинации
     */
    public static final String paginate = ApxStoreConsts.paginate;

    /**
     * Имя свойства с информацией об orderBy
     */
    public static final String orderBy = "orderBy";

    /**
     * Имя свойства с информацией об id.
     * Наличие такого ключа в параметрах означает, что пользоатель запрашивает одну
     * запись. В этом случае должен быть фильтр с ключем id и в результате должна
     * загрузится только одна запись.
     */
    public static final String id = "id";

}
