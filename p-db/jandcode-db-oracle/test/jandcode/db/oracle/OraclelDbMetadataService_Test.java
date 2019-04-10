package jandcode.db.oracle;

import jandcode.core.test.*;
import jandcode.db.*;
import jandcode.db.test.*;
import jandcode.commons.named.*;
import org.junit.jupiter.api.*;

public class OraclelDbMetadataService_Test extends App_Test {

    DbSimpleTestSvc z = testSvc(DbSimpleTestSvc.class);

    @Test
    public void test_close() throws Exception {
        stopwatch.start();
        DbMetadataService svc = z.getDb().getDbSource().bean(DbMetadataService.class);
        NamedList<DbMetadataTable> lst = svc.getTables();
        for (DbMetadataTable t : lst) {
            System.out.println("table: " + t.getName());
            for (DbMetadataField f : t.getFields()) {
                System.out.println(f.getName() + ":" + f.getDbDatatype().getName() + "(" + f.getSize() + ")");
            }
        }
        stopwatch.stop();
    }


}
