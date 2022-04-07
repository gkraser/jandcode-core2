package jandcode.core.dbm.ddl.impl;

import jandcode.commons.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;

import java.util.*;

/**
 * Утилитки для DDL
 */
public class DDLUtils {

    /**
     * Создает список {@link DDLOper} из скрипта sql
     *
     * @param model  модель
     * @param script скрипт
     */
    public static List<DDLOper> createFromSqlScript(Model model, String script) {
        List<DDLOper> res = new ArrayList<>();
        //
        DDLService svc = model.bean(DDLService.class);
        List<String> lst = UtSql.splitScript(script);
        int n = 0;
        for (String sq : lst) {
            n++;
            DDLOper_sql a = (DDLOper_sql) svc.createOperInst("sql");
            a.setSqlText(sq);
            a.setName("n" + n);
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
