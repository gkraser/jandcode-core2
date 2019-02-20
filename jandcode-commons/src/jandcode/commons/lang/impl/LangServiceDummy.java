package jandcode.commons.lang.impl;

import jandcode.commons.lang.*;

import java.text.*;

/**
 * Заглушка для переводчика
 */
public class LangServiceDummy implements ILangService {

    public String t(String s, Object... params) {
        if (s == null) {
            return "";
        }
        if (params == null || params.length == 0) {
            return s;
        } else {
            return MessageFormat.format(s, params);
        }
    }

}
