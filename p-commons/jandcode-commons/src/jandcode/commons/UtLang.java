package jandcode.commons;

import jandcode.commons.lang.*;
import jandcode.commons.lang.impl.*;

import java.text.*;

/**
 * Статический класс для поддержки локализации приложений
 */
public class UtLang {

    private static ILangService inst = new LangServiceDummy();

    //////

    public static ILangService getInst() {
        return inst;
    }

    public static void setInst(ILangService inst) {
        UtLang.inst = inst;
    }

    //////

    /**
     * Перевод строки на текущий язык
     *
     * @param s      исходная строка
     * @param params параметры для {@link MessageFormat#format(String, Object...)}
     * @return переведенная строка
     */
    public static String t(String s, Object... params) {
        return inst.t(s, params);
    }

}
