package jandcode.db.mysql;

import jandcode.core.test.*;
import jandcode.db.*;
import jandcode.db.test.*;
import org.junit.jupiter.api.*;

public class MysqlDbQuery_Test extends App_Test {

    DbSimpleTestSvc z = testSvc(DbSimpleTestSvc.class);

    @Test
    public void test_close() throws Exception {
        Db db = z.getDb();
        DbQuery q = db.openQuery("select * from test1 where id<4 order by id ");
        try {
            while (!q.eof()) {
                System.out.println(q.getValue("id"));
                q.next();
            }
        } finally {
            q.close();
        }
    }

    @Test
    public void test_autoClose() throws Exception {
        Db db = z.getDb();
        try (DbQuery q = db.openQuery("select * from test1 where id<4 order by id ")) {
            while (!q.eof()) {
                System.out.println(q.getValue("id"));
                q.next();
            }
        }
    }


}
