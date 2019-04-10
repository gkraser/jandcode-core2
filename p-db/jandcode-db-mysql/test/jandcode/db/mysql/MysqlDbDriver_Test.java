package jandcode.db.mysql;

import jandcode.core.test.*;
import jandcode.db.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MysqlDbDriver_Test extends App_Test {

    @Test
    public void test_initConnectionSqls() throws Exception {
        DbService svc = app.bean(DbService.class);
        DbSource dbs = svc.getDbSource("test.1");
        List<String> sqls = dbs.getDbDriver().getInitConnectionSqls();
        assertEquals(sqls.size(), 0);
    }


}
