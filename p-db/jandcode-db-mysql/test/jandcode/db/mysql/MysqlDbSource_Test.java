package jandcode.db.mysql;

import jandcode.core.test.*;
import jandcode.db.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MysqlDbSource_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        DbSource dbs = app.bean(DbService.class).getDbSource("test.1");
        dbs.getProps().forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
        assertEquals(dbs.getProps().get("dbcp.validationQuery"), "select 1");
    }


}
