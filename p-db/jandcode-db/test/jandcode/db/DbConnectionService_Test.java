package jandcode.db;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import java.sql.*;

public class DbConnectionService_Test extends App_Test {

    @Test
    public void direct1() throws Exception {
        DbService svc = app.bean(DbService.class);
        DbSource dbs = svc.getDbSource("test.h2");
        utils.outMap(dbs.getConf());
        //
        DbConnectionService connSvc = (DbConnectionService) dbs.bean(DbConsts.BEAN_DIRECT_CONNECT);
        //
        Connection cn = connSvc.connect();
        //
    }


}
