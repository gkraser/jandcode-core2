package jandcode.commons;

import java.util.*;

/**
 * Утилиты для текста sql
 */
public class UtSql {

    /**
     * Разделитель в скриптах по умолчанию
     */
    public static final String SCRIPT_DELIMITER = "~~";

    /**
     * Разбивает sql-скрипт на список
     *
     * @param text      текст sql-скрипта
     * @param delimiter разделитель. Если не задан, используется {@link UtSql#SCRIPT_DELIMITER}
     * @return список sql. Пустые sql исключаются из списка
     */
    public static List<String> splitScript(String text, String delimiter) {
        List<String> res = new ArrayList<>();
        if (UtString.empty(text)) {
            return res;
        }
        if (UtString.empty(delimiter)) {
            delimiter = SCRIPT_DELIMITER;
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

    /**
     * Разбивает sql-скрипт на список. Разделитель: '~~'.
     *
     * @param text текст sql-скрипта
     * @return список sql. Пустые sql исключаются из списка
     */
    public static List<String> splitScript(String text) {
        return splitScript(text, null);
    }

}
