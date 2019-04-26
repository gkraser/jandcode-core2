package jandcode.db;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class DbDriver_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        DbDriverService svc = app.bean(DbDriverService.class);

        for (DbDriverDef dd : svc.getDbDrivers()) {
            System.out.println(dd.getName());
            for (DbDataType dt : dd.getInst().getDbDataTypes()) {
                System.out.println("  " + dt.getName() + ":" + dt.getClass());
            }
        }
    }


}
