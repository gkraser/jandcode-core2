package jandcode.db;

/**
 * Некоторые имена свойств dbsource как константы
 */
public class DbSourcePropsConsts {

    /**
     * Свойство 'jdbcDriverClass'. Класс jdbc-драйвера. Обычно явно не задается
     * и определяется конкретным драйвером базы данных.
     */
    public static final String jdbcDriverClass = "jdbcDriverClass";

    /**
     * Имя базы данных
     */
    public static final String database = "database";

    /**
     * Хост
     */
    public static final String host = "host";

    /**
     * Имя пользователя
     */
    public static final String username = "username";

    /**
     * Пароль
     */
    public static final String password = "password";

    /**
     * url коннекта
     */
    public static final String url = "url";

    /**
     * Префикс для initConnectionSql
     */
    public static final String initConnectionSql = "initConnectionSql";

}
