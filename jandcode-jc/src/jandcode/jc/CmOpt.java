package jandcode.jc;

import jandcode.commons.named.*;

/**
 * Опция команды
 */
public interface CmOpt extends INamed {

    /**
     * Описание опции
     */
    String getHelp();

    /**
     * Значение опции по умолчанию. Фактически определяет тип значения опции.
     */
    Object getDefaultValue();

}
