package jandcode.core.dbm.sql;

import java.util.*;

/**
 * Класс для хранения текста sql и манипуляций с ними.
 * <p>
 * Метод toString() возвращает преобразованный sql.
 */
public interface SqlText extends CharSequence {

    /**
     * Установить sql текст
     */
    void setSql(String sql);

    /**
     * Оригинальный текст sql, до преобразований.
     * Тот, который был установлен при создании объекта или
     * методом {@link SqlText#setSql(java.lang.String)}.
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


    ////// replace parts

    /**
     * Заменить where.
     * Вызов метода отменяет предыдущий вызов replaceWhere для указанного whereName.
     *
     * @param whereName  имя where, по умолчанию 'default'
     * @param whereTexts условия where, между ними ставится 'and'
     */
    void replaceWhere(String whereName, List<String> whereTexts);

    /**
     * см. {@link SqlText#replaceWhere(java.lang.String, java.util.List)},
     * где whereName=default
     */
    default void replaceWhere(List<String> whereTexts) {
        replaceWhere(null, whereTexts);
    }

    /**
     * Заменить поля в select.
     * Вызов метода отменяет предыдущий вызов replaceWhere.
     *
     * @param text   на что заменить
     * @param append false - заменить текст полностью, true - добавить текст
     */
    void replaceSelect(String text, boolean append);

    /**
     * Заменить order by.
     * Вызов метода отменяет предыдущий вызов replaceWhere.
     *
     * @param text на что. Если пусто - order by удаляется
     */
    void replaceOrderBy(String text);

}
