package jandcode.commons.str.impl;

import jandcode.commons.*;

/**
 * Дедубликатор строк в camelCase. Возвращает внутреннюю кешированную копию
 * запрошенной строки в camelCase.
 */
public class CameCaseStrDedup extends BaseStrDedup {

    public String dedup(String s) {
        if (s == null) {
            return "";
        }
        String s1 = cache.get(s);
        if (s1 != null) {
            return s1;
        }
        s1 = UtString.camelCase(s);
        cache.put(s, s1);
        return s1;
    }

}
