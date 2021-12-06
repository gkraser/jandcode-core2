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
     * @return ссылка на себя
     */
    SqlText paginate(boolean v);

    /**
     * Префикс имен параметров для пагинации.
     * По умолчанию параметры без префиксов: offset и limit.
     *
     * @param prefix префикс для имен параметров
     * @return ссылка на себя
     */
    SqlText paginateParamsPrefix(String prefix);


    ////// replace parts

    /**
     * Заменить where.
     * Вызов метода отменяет предыдущий вызов replaceWhere для указанного whereName.
     *
     * @param whereName  имя where, по умолчанию 'default'
     * @param whereTexts условия where, между ними ставится 'and'
     * @return ссылка на себя
     */
    SqlText replaceWhere(String whereName, List<String> whereTexts);

    /**
     * см. {@link SqlText#replaceWhere(java.lang.String, java.util.List)},
     * где whereName=default
     */
    default SqlText replaceWhere(List<String> whereTexts) {
        return replaceWhere(null, whereTexts);
    }

    /**
     * см. {@link SqlText#replaceWhere(java.lang.String, java.util.List)},
     * где whereTexts это список из одного элемента whereText
     */
    default SqlText replaceWhere(String whereName, String whereText) {
        return replaceWhere(whereName, List.of(whereText));
    }

    /**
     * см. {@link SqlText#replaceWhere(java.util.List)},
     * где whereTexts это список из одного элемента whereText
     */
    default SqlText replaceWhere(String whereText) {
        return replaceWhere(null, List.of(whereText));
    }

    /**
     * Заменить поля в select.
     * Вызов метода отменяет предыдущий вызов replaceWhere.
     *
     * @param text   на что заменить
     * @param append false - заменить текст полностью, true - добавить текст
     * @return ссылка на себя
     */
    SqlText replaceSelect(String text, boolean append);

    /**
     * см. {@link SqlText#replaceSelect(java.lang.String, boolean)},
     * где append=false
     */
    default SqlText replaceSelect(String text) {
        return replaceSelect(text, false);
    }

    /**
     * Заменить order by.
     * Вызов метода отменяет предыдущий вызов replaceWhere.
     *
     * @param text на что. Если пусто - order by удаляется
     * @return ссылка на себя
     */
    SqlText replaceOrderBy(String text);

}
