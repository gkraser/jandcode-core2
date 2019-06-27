package jandcode.commons.str;

import jandcode.commons.*;

/**
 * Обработчик переменных в {@link UtString#substVar(java.lang.String, java.lang.String, java.lang.String, ISubstVar)}.
 */
public interface ISubstVar {

    /**
     * Реакция на переменную v. Вместо нее нужно вернуть то, что будет подставленно в
     * раскрываемую строку
     *
     * @param v переменная
     * @return значение переменной
     */
    String onSubstVar(String v);

}
