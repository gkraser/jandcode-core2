package jandcode.jsa.jsmodule;

import jandcode.commons.*;
import jandcode.commons.error.*;

import java.util.*;

/**
 * Статические утилиты для jsmodule
 */
public class JsModuleUtils {

    /**
     * Сколько символов из md5 берем для id
     */
    public static final int ID_MD5_SIZE = 12;

    /**
     * Размер id в символах
     */
    public static final int ID_SIZE = 8;

    /**
     * Путь превращает в id
     *
     * @param path нормализованный путь
     * @return id
     */
    public static String pathToId(String path) {
        String md = UtString.md5Str(path);
        md = md.substring(0, ID_MD5_SIZE);
        long x1 = Long.parseLong(md, 16);
        String id = UtCnv.toRadix(x1, 64, ID_SIZE);
        return id;
    }

    /**
     * Переводит строку со склееными id в список id.
     * Т.е. строку вида "xxxxxxxxYYYYYYYY" в список ["xxxxxxxx", "YYYYYYYY"].
     */
    public static List<String> strToIdList(String s) {
        List<String> res = new ArrayList<>();
        if (UtString.empty(s)) {
            return res;
        }
        int a = s.length() % ID_SIZE;
        if (a != 0) {
            throw new XError("Неправильная строка с id");
        }
        a = 0;
        while (a < s.length()) {
            int b = a + 8;
            String id = s.substring(a, b);
            res.add(id);
            a = b;
        }
        return res;
    }

}
