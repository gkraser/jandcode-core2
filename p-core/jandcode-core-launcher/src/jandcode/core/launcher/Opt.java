package jandcode.core.launcher;

import jandcode.commons.named.*;

/**
 * Опция командной строки
 */
public interface Opt extends INamed {

    /**
     * Имя
     */
    String getName();

    /**
     * Помощь. Полный текст, возможно многострочный.
     */
    String getDesc();

    /**
     * Имеется ли аргумент
     */
    boolean hasArg();

    /**
     * Имя аргумента. Если аргумента нет - пустая строка.
     */
    String getArgName();

    /**
     * Значение аргумента по умолчанию.
     */
    String getDefaultArgValue();

}
