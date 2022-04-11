package jandcode.core.dbm.ddl.impl;

import jandcode.core.dbm.ddl.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

/**
 * DDLProvider по умолчанию. Рассматривает свой тег, как данные для
 * получения sql по правилам: {@link ISqlService#createSqlText(jandcode.commons.conf.Conf)}
 */
public class DefaultDDLProvider extends BaseDDLProvider {

    private boolean used;

    protected void onLoad(List<DDLOper> res, DDLStage stage) throws Exception {
        if (used) {
            return; // должно отрабатывать только один раз
        }
        used = true;
        SqlText sql = getModel().bean(SqlService.class).createSqlText(getConf(), getContext());
        List<DDLOper> a = DDLUtils.createFromSqlScript(getModel(), sql.toString());
        res.addAll(a);
    }

}
