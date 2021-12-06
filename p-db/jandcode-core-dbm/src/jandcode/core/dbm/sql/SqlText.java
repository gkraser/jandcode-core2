package jandcode.core.dbm.sql;

/**
 * Класс для хранения текста sql и манипуляций с ними.
 * <p>
 * Метод toString() возвращает преобразованный
 */
public interface SqlText {

    /**
     * Установить sql текст
     */
    void setSql(String sql);

    /**
     * Оригинальный текст sql, до преобразований.
     * Тот, который был установлен методом {@link SqlText#setSql(java.lang.String)}.
     */
    String getOrigSql();

    /**
     * Преобразованный текст sql.
     * Метод toString() возвращает это значение.
     */
    String getSql();


    ////// paginate

    /**
     * Пагинация.
     *
     * @param v true - включить пагинацию
     */
    void paginate(boolean v);

    /**
     * @see SqlText#paginate(boolean)
     */
    void setPaginate(boolean v);

    /**
     * Префикс имен параметров для пагинации.
     * По умолчанию параметры без префиксов: offset и limit.
     *
     * @param prefix префикс для имен параметров
     */
    void paginateParamsPrefix(String prefix);

}
