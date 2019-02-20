package jandcode.jc;

import groovy.lang.*;

import java.util.*;

/**
 * Хранилище команд проекта и построитель команд.
 */
public interface CmHolder {

    /**
     * Список всех команд в хранилище
     */
    Collection<Cm> getItems();

    /**
     * Получить окончательный список опций для команды с учетом
     * динамических опций
     *
     * @param cm   для какой команды
     * @param args аргументы командной строки
     * @return список опций
     */
    Collection<CmOpt> getOpts(Cm cm, Map args);


    /**
     * Добавить команду
     *
     * @param cmName команда
     * @param args   Свойства команды.
     *               Строковый агрумент распознается как помощь (может быть только один, обычно первый).
     *               {@link CmOpt} распознается как опция (может быть сколько угодно).
     *               Closure распознается как код выполнения (может быть только одна, обычно последний
     *               элемент в списке).
     *               List разворачивается и для каждого элемента работают указанные выше правила.
     * @return добавленная команда
     */
    Cm add(String cmName, Object... args);

    /**
     * Удалить команду
     */
    void remove(String cmName);

    /**
     * Найти команду по имени
     *
     * @return null, если нет команды
     */
    Cm find(String cmName);

    /**
     * Найти команду по имени
     *
     * @return error, если нет команды
     */
    Cm get(String cmName);

    /**
     * Выполнить команду
     */
    void exec(String cmName);

    /**
     * Выполнить команду с аргументами
     */
    void exec(String cmName, Map args);

    /**
     * Выполнить команду с аргументами (для groovy синтаксиса exec("cm", a:1, b:2))
     */
    void exec(Map args, String cmName);

    /**
     * Создать экземпляр опции для команды
     *
     * @param name         имя опции
     * @param defaultValue Значение по умолчанию. Фактически определяет тип значения опции.
     * @param help         помощь
     * @return экземпляр опции
     */
    CmOpt opt(String name, Object defaultValue, String help);

    /**
     * Создать экземпляр boolean-опции для команды
     *
     * @param name имя опции
     * @param help помощь
     * @return экземпляр опции
     */
    CmOpt opt(String name, String help);

    /**
     * Создать экземпляр динамического формирователя опций
     *
     * @param name имя опции. Не используется в дальнейшем, просто для обеспечения
     *             уникальности
     * @param cls  closure. Сигнатура {@code List<CmOpt> cls(Map args)}.
     *             args: сырые аргументы командной строки, полученные из main.
     *             Должна вернуть список дополнительных опций для команды.
     */
    CmOpt opt(String name, Closure cls);

}
