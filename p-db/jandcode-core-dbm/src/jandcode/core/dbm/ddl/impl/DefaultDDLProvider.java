package jandcode.core.dbm.ddl.impl;

import jandcode.core.dbm.ddl.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

/**
 * DDLProvider по умолчанию. Рассматривает свой тег, как данные для
 * получения sql по правилам: {@link ISqlService#createSqlText(jandcode.commons.conf.Conf)}
 */
public class DefaultDDLProvider extends BaseDDLProvider {

    protected void onLoad(List<DDLOper> res, DDLStage stage) throws Exception {
        SqlText sql = getModel().bean(SqlService.class).createSqlText(getConf());
        List<DDLOper> a = DDLUtils.createFromSqlScript(getModel(), sql.toString(), getBaseName());
        res.addAll(a);
    }

}
