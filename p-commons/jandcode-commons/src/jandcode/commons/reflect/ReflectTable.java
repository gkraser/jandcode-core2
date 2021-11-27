package jandcode.commons.reflect;

import java.util.*;

/**
 * Структура таблицы для класса.
 * <p>
 * Это взгляд на некий класс, как на строку таблицы.
 * Каждое свойство класса - поле таблицы. Свойство представленно соответствующим getter.
 */
public interface ReflectTable {

    /**
     * Реальный класс
     */
    Class getCls();

    /**
     * Поля. Список потенциально может быть пустым.
     */
    Collection<ReflectTableField> getFields();

    /**
     * Найти поле по имени без учета регистра.
     */
    ReflectTableField findField(String name);

}
