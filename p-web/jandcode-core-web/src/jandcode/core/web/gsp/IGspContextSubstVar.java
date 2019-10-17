package jandcode.core.web.gsp;

/**
 * ИНтерфейс для классов, которые желают раскрывать
 * подстановки в {@link GspContext#substVar(java.lang.String)}.
 */
public interface IGspContextSubstVar {

    /**
     * Вернуть значение переменной. Если переменная неизвестная, возвращает null.
     */
    String handleSubstVar(String v);

}
