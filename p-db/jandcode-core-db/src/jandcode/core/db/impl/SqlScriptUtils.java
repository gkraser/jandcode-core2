package jandcode.core.db.impl;

import jandcode.commons.*;
import jandcode.core.db.*;

import java.util.*;

/**
 * Статические утилиты для скриптов
 */
public class SqlScriptUtils {

    /**
     * Разбивает sql-скрипт на список
     *
     * @param text      текст sql-скрипта
     * @param delimiter разделитель. Если не задан, используется {@link DbConsts#SCRIPT_DELIMITER}
     * @return список sql. Пустые sql исключаются из списка
     */
    public static List<String> splitSqlScript(String text, String delimiter) {
        List<String> res = new ArrayList<>();
        if (UtString.empty(text)) {
            return res;
        }
        if (UtString.empty(delimiter)) {
            delimiter = DbConsts.SCRIPT_DELIMITER;
        }
        StringBuilder sbDelim = new StringBuilder();
        for (int i = 0; i < delimiter.length(); i++) {
            sbDelim.append("\\");
            sbDelim.append(delimiter.charAt(i));
        }
        String[] ar = text.split(sbDelim.toString());

        for (String sql1 : ar) {
            String sql2 = sql1.trim();
            if (sql2.length() == 0) {
                continue;
            }
            res.add(sql2);
        }

        return res;
    }


}
