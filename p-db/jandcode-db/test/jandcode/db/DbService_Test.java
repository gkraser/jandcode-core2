package jandcode.db;

import jandcode.core.test.*;
import jandcode.commons.conf.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbService_Test extends App_Test {

    @Test
    public void test_resolveDbDriver() throws Exception {
        DbService svc = app.bean(DbService.class);

        Conf x = app.getConf().getConf("db/dbsource/test.cfg.1");
        DbDriverDef drv = svc.resolveDbDriver(x);
        assertEquals(drv.getDbType(), "tmp.jdbc.dbtype");
    }

}
