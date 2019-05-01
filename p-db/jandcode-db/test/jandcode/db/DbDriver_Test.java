package jandcode.db;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class DbDriver_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        DbDriverService svc = app.bean(DbDriverService.class);

        for (String dn : svc.getDbDriverNames()) {
            System.out.println(dn);
            DbDriver dd = svc.getDbDriver(dn);
            for (DbDataType dt : dd.getDbDataTypes()) {
                System.out.println("  " + dt.getName() + ":" + dt.getClass());
            }
        }
    }


}
