package jandcode.commons.str.impl;

/**
 * Нормальный дедубликатор. Возвращает внутреннюю кешированную копию
 * запрошенной строки.
 */
public class NormalStrDedup extends BaseStrDedup {

    public String dedup(String s) {
        if (s == null) {
            return "";
        }
        String s1 = cache.get(s);
        if (s1 != null) {
            return s1;
        }
        cache.put(s, s);
        return s;
    }

}
