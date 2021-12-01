package jandcode.core.db;

/**
 * Константы для db.
 */
public class DbConsts {

    /**
     * Драйвер по умолчанию
     */
    public static final String DBDRIVER_DEFAULT = "base";

    /**
     * Бин для прямого соединения, без пула.
     */
    public static final String BEAN_DIRECT_CONNECT = "connectionService.direct";

    /**
     * Префикс для имен полей в результатах запроса, которые будут игнорироватся
     * и не попадать в структуру {@link DbQuery}.
     */
    public static final String IGNORE_FIELD_PREFIX = "zzz___";

}
