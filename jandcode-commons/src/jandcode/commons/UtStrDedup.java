package jandcode.commons;

import jandcode.commons.str.*;
import jandcode.commons.str.impl.*;

/**
 * Утилиты для дедублицирования строки
 */
public class UtStrDedup {

    public static StrDedup DEDUP_NORMAL = new NormalStrDedup();
    public static StrDedup DEDUP_LOWER = new LowerStrDedup();


    /**
     * Возвращает дедублицированную строку без ее изменения.
     */
    public static String normal(String s) {
        return DEDUP_NORMAL.dedup(s);
    }

    /**
     * Возвращает дедублицированный lowercase вариант строки.
     */
    public static String lower(String s) {
        return DEDUP_LOWER.dedup(s);
    }

}
