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

}
