package jandcode.db.mariadb

import jandcode.commons.*
import jandcode.commons.datetime.*
import jandcode.db.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Mariadb_DbDatatType_Test extends DbDatatypeTestCase {

    @Test
    public void test1() throws Exception {
        //sqltypes()
        //storetypes()
        //test_memo()

        //assertEquals(z.dbdatatypeResult("memo"), "memo")

        //assertEquals(z.dbdatatypeResult("datetime"), "datetime")

        //test_date()

        ///////

        assertEquals(z.dbdatatypeResult("date"), "datetime")
        assertEquals(z.dbdatatypeResult("datetime"), "datetime")

        XDateTime dt;

        dt = UtDateTime.create("2012-11-30")
        assertEquals(z.dbdatatypeRetrive("date", dt), dt)

        //test_date()

    }

    @Test
    public void test2() throws Exception {
        sqltypes()
        storetypes()
        //assertEquals(z.dbdatatypeIsNull("date"), true)
    }

    @Test
    public void test_date() throws Exception {
        assertEquals(z.dbdatatypeResult("date"), "datetime")
        //
        XDateTime dt;

        assertEquals(z.dbdatatypeIsNull("date"), true)
        //assertEquals(z.dbdatatypeRetrive("date", null), null)

        dt = UtDateTime.create("2012-11-30")
        assertEquals(z.dbdatatypeRetrive("date", dt), dt)

        dt = UtDateTime.now()
        assertEquals(z.dbdatatypeRetrive("date", dt), dt.clearTime())

//        dt = UtDateTime.create("1984-04-01")
//        assertEquals(z.dbdatatypeRetrive("date", dt), dt)

    }

    @Test
    public void test_datetime() throws Exception {
        assertEquals(z.dbdatatypeResult("datetime"), "datetime")
        //
        XDateTime dt;

        dt = UtDateTime.create("2012-11-30T22:23:24")
        assertEquals(z.dbdatatypeRetrive("datetime", dt), dt)

        dt = UtDateTime.now()
        assertEquals(z.dbdatatypeRetrive("datetime", dt), dt.clearMSec())

        dt = UtDateTime.create("1984-04-01")
        if (z.dbType == "derby" || z.dbType == "firebird") {
            assertEquals(z.dbdatatypeRetrive("datetime", dt), UtDateTime.create("1984-04-01T01:00:00"))
        } else {
            assertEquals(z.dbdatatypeRetrive("datetime", dt), dt)
        }

        dt = UtDateTime.create("1984-04-01T15:16:17")
        assertEquals(z.dbdatatypeRetrive("datetime", dt), dt)

        assertEquals(z.dbdatatypeRetrive("datetime", null), null)
    }

}
