package jandcode.db.dbsource;

import jandcode.core.test.*;
import jandcode.db.*;
import jandcode.db.h2.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbSource_Test extends App_Test {

    DbService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(DbService.class);
    }

    @Test
    public void test1() throws Exception {
        DbSource dbs = svc.createDbSource("test1");
        utils.delim("conf");
        utils.outMap(dbs.getConf());
        utils.delim("prop");
        utils.outMap(dbs.getProps());
        //
        assertEquals(dbs.getConf().getString("param1"), "1");
        assertEquals(dbs.getConf().getString("dbdriver"), "h2.mem");
        assertEquals(dbs.getDbType(), "h2");
        assertTrue(dbs.getDbDriver() instanceof H2DbDriver);

    }

    @Test
    public void props1() throws Exception {
        DbSource dbs = svc.createDbSource("test1");
        utils.delim("prop");
        utils.outMap(dbs.getProps());
        //
        assertFalse(dbs.getProps().containsKey("bean"));

    }

}
