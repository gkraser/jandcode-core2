package jandcode.db;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbSource_Test extends App_Test {

    @Test
    public void test_cfg() throws Exception {
        DbService svc = app.bean(DbService.class);
        DbSource dbs = svc.getDbSource("test.cfg.1");
        //System.out.println(dbs.getProps());
        assertEquals(dbs.getProps().get("flag1"), "1");
        assertEquals(dbs.getProps().get("flag2"), "2");
        assertEquals(dbs.getDbType(), "tmp.jdbc.dbtype");
    }


}
