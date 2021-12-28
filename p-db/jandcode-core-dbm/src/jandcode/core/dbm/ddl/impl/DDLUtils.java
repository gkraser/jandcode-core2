package jandcode.core.dbm.ddl.impl;

import jandcode.commons.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;

import java.util.*;
import java.util.regex.*;

/**
 * Утилитки для DDL
 */
public class DDLUtils {

    /**
     * Префикс имени ddl.
     * Если в тексте sql первым идет эта конструкция, то все что поле нее и до
     * конца строки - имя этого ddl-оператора.
     */
    public static final String PREFIX_DDL_NAME = "--@";

    /**
     * Шаблон для выделения имени из sql текста
     */
    public static Pattern PATTERN_DDL_NAME = Pattern.compile("^\\-\\-@(.*?)[\\n\\r](.*)", Pattern.DOTALL);

    /**
     * Создает список {@link DDLOper} из скрипта sql
     *
     * @param model    модель
     * @param script   скрипт
     * @param baseName базовое имя
     */
    public static List<DDLOper> createFromSqlScript(Model model, String script, String baseName) {
        List<DDLOper> res = new ArrayList<>();
        //
        if (!UtString.empty(baseName)) {
            baseName = baseName + ".";
        }
        //
        DDLService svc = model.bean(DDLService.class);
        List<String> lst = UtSql.splitScript(script);
        int n = 0;
        for (String sq : lst) {
            n++;
            String sfx = "";
            String s1 = sq;
            Matcher m = PATTERN_DDL_NAME.matcher(sq);
            if (m.find()) {
                s1 = m.group(2);
                sfx = m.group(1);
            } else {
                sfx = "" + n;
            }
            DDLOper_sql a = (DDLOper_sql) svc.createOperInst("sql");
            a.setSqlText(s1);
            a.setName(baseName + sfx);
            res.add(a);
        }

        //
        return res;
    }


}
