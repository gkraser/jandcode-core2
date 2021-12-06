package jandcode.commons.str.impl;

import jandcode.commons.*;

/**
 * Дедубликатор строк в snake_case. Возвращает внутреннюю кешированную копию
 * запрошенной строки в snake_case.
 */
public class SnakeCaseStrDedup extends BaseStrDedup {

    public String dedup(String s) {
        if (s == null) {
            return "";
        }
        String s1 = cache.get(s);
        if (s1 != null) {
            return s1;
        }
        s1 = UtString.unCamelCase(s);
        cache.put(s, s1);
        return s1;
    }

}
