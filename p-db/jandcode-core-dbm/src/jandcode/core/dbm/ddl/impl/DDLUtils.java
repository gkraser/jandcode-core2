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
     * Если в тексте sql первым идет эта конструкция, то все что после нее и до
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

    /**
     * Извлечение имени домена из имени файла: первое слово до '-' или до '.'.
     */
    public static String fileNameToDomainName(String filename) {
        String fn = UtFile.filename(filename);

        // убираем расширения
        int a = fn.indexOf('.');
        if (a != -1) {
            fn = fn.substring(0, a);
        }

        // убираем после '-'
        a = fn.indexOf('-');
        if (a != -1) {
            fn = fn.substring(0, a);
        }

        return fn;
    }

    /**
     * Извлечение имени StoreLoader из имени файла.
     * Это либо расширение, либо второе расширение, если из 2 и более.
     */
    public static String fileNameToStoreLoaderName(String filename) {
        String fn = UtFile.filename(filename);

        String ext = UtFile.ext(fn);
        if (UtString.empty(ext)) {
            return fn;
        }
        fn = UtFile.removeExt(fn);
        String ext2 = UtFile.ext(fn);
        if (!UtString.empty(ext2)) {
            return ext2;
        }
        return ext;
    }


}
