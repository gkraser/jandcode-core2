package jandcode.commons;

import jandcode.commons.str.*;
import jandcode.commons.str.impl.*;

/**
 * Утилиты для дедублицирования строки
 */
public class UtStrDedup {

    public static StrDedup DEDUP_NORMAL = new NormalStrDedup();
    public static StrDedup DEDUP_LOWER = new LowerStrDedup();
    public static StrDedup DEDUP_CAMEL_CASE = new CameCaseStrDedup();
    public static StrDedup DEDUP_SNAKE_CASE = new SnakeCaseStrDedup();


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

    /**
     * Возвращает дедублицированный camelCase вариант строки.
     */
    public static String camelCase(String s) {
        return DEDUP_CAMEL_CASE.dedup(s);
    }

    /**
     * Возвращает дедублицированный snakeCase вариант строки.
     */
    public static String snakeCase(String s) {
        return DEDUP_SNAKE_CASE.dedup(s);
    }

}
