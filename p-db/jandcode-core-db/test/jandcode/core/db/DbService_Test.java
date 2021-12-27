package jandcode.core.db;

import jandcode.commons.conf.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbService_Test extends App_Test {

    @Test
    public void createDbSource1() throws Exception {
        DbService svc = app.bean(DbService.class);
        Conf conf = Conf.create();
        DbSource dbs = svc.createDbSource(conf);
        utils.outMap(dbs.getConf());
    }

    @Test
    public void createDbSource_byName() throws Exception {
        DbService svc = app.bean(DbService.class);
        DbSource dbs = svc.createDbSource("dbSource_byName");
        assertEquals(dbs.getConf().getString("param1"), "1");
    }

    @Test
    public void getDbSource_byName() throws Exception {
        DbService svc = app.bean(DbService.class);
        DbSource dbs = svc.getDbSource("dbSource_byName");
        DbSource dbs2 = svc.getDbSource("dbSource_byName");
        assertEquals(dbs.getConf().getString("param1"), "1");
        assertSame(dbs, dbs2);
        DbSource dbs3 = svc.createDbSource("dbSource_byName");
        assertNotSame(dbs, dbs3);
    }

}
