package jandcode.core.dbm.sql;

import jandcode.core.dbm.*;

import java.util.*;

/**
 * Класс для хранения текста sql и манипуляций с ними.
 * <p>
 * Метод toString() возвращает преобразованный sql.
 */
public interface SqlText extends CharSequence, IModelLink {

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

    /**
     * Клонировать объект
     */
    SqlText cloneSqlText();

    /**
     * Возвращает SqlText, где select заменен на 'count(*) as countFieldName'
     *
     * @param countFieldName имя поля для `count(*)`
     * @return новый экземпляр
     */
    SqlText asCountSqlText(String countFieldName);

    /**
     * Возвращает SqlText, где select заменен на 'count(*) as cnt'
     *
     * @return новый экземпляр
     */
    default SqlText asCountSqlText() {
        return asCountSqlText("cnt");
    }


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

    /**
     * Включена ли пагинация
     */
    boolean isPaginate();


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
     * Добавить условие where. Существующие условия не изменяются.
     *
     * @param whereName имя where, по умолчанию 'default'
     * @param whereText условие where
     * @return ссылка на себя
     */
    SqlText addWhere(String whereName, String whereText);

    /**
     * Добавить условие для where с именем 'default'. Существующие условия не изменяются.
     *
     * @param whereText условие where
     * @return ссылка на себя
     */
    default SqlText addWhere(String whereText) {
        return addWhere(null, whereText);
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

    //////

    /**
     * Заменить part.
     * Вызов метода отменяет предыдущий вызов replacePart для указанного partName.
     *
     * @param partName  имя part, по умолчанию 'default'
     * @param partTexts тексты part, между ними ставится ' ' (пробел)
     * @return ссылка на себя
     */
    SqlText replacePart(String partName, List<String> partTexts);

    /**
     * см. {@link SqlText#replacePart(java.lang.String, java.util.List)},
     * где partName=default
     */
    default SqlText replacePart(List<String> partTexts) {
        return replacePart(null, partTexts);
    }

    /**
     * см. {@link SqlText#replacePart(java.lang.String, java.util.List)},
     * где partTexts это список из одного элемента partText
     */
    default SqlText replacePart(String partName, String partText) {
        return replacePart(partName, List.of(partText));
    }

    /**
     * см. {@link SqlText#replacePart(java.util.List)},
     * где partTexts это список из одного элемента partText
     */
    default SqlText replacePart(String partText) {
        return replacePart(null, List.of(partText));
    }

    /**
     * Добавить условие part. Существующие условия не изменяются.
     *
     * @param partName имя part, по умолчанию 'default'
     * @param partText условие part
     * @return ссылка на себя
     */
    SqlText addPart(String partName, String partText);

    /**
     * Добавить условие для part с именем 'default'. Существующие условия не изменяются.
     *
     * @param partText условие part
     * @return ссылка на себя
     */
    default SqlText addPart(String partText) {
        return addPart(null, partText);
    }

}
