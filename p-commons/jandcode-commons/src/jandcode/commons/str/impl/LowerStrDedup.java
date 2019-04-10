package jandcode.commons.str.impl;

/**
 * Дедубликатор строк в нижнем регистре. Возвращает внутреннюю кешированную копию
 * запрошенной строки в lowercase.
 */
public class LowerStrDedup extends BaseStrDedup {

    public String dedup(String s) {
        if (s == null) {
            return "";
        }
        String s1 = cache.get(s);
        if (s1 != null) {
            return s1;
        }
        s1 = s.toLowerCase();
        cache.put(s, s1);
        return s1;
    }

}
