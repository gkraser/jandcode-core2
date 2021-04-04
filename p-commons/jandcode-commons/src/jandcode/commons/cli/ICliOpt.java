package jandcode.commons.cli;

import java.util.*;

/**
 * Свойства опции командной строки.
 */
public interface ICliOpt {

    /**
     * Уникальный ключ.
     */
    String getKey();

    /**
     * Имена опции. Например ['-d', '--debug'].
     * Тире обязательны.
     * <p>
     * Если имен нет, то считается что это позиционный параметр.
     */
    List<String> getNames();

    /**
     * Описание опции
     */
    String getDesc();

    /**
     * Имеется ли аргумент. Если нет, то это boolean.
     */
    boolean hasArg();

    /**
     * Имя аргумента. Если аргумента нет - пустая строка.
     */
    String getArg();

    /**
     * Имеется ли значение по умолчанию.
     */
    boolean hasDefaultValue();

    /**
     * Значение по умолчанию для аргумента.
     * Это не само значение, а то, что будет напечатано при показа помощи.
     */
    String getDefaultValue();

    /**
     * При значении true опция может повторятся несколько раз.
     */
    boolean isMulti();

    /**
     * Позиционный параметр, если нет имен в {@link ICliOpt#getNames().
     */
    boolean isPositional();

    /**
     * Обязательный параметр
     */
    boolean isRequired();

}
