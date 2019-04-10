package jandcode.db.firebird

import jandcode.db.test.*
import jandcode.commons.*
import jandcode.commons.datetime.XDateTime
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

//@Ignore
class FirebirdDbDatatype_Test extends JdbcDbDatatypeTestCase {

    @Test
    public void test1() throws Exception {
    }

    @Test
    public void test_datetime_84() throws Exception {
        XDateTime dt;

        dt = UtDateTime.create("1984-04-01")
        //assertEquals(z.dbdatatypeRetrive("datetime", dt), dt)
        assertEquals(z.dbdatatypeRetrive("datetime", dt), UtDateTime.create("1984-04-01T01:00:00"))

    }

    @Test
    public void test_date_84() throws Exception {
        XDateTime dt;

        dt = UtDateTime.create("1984-04-01")
        assertEquals(z.dbdatatypeRetrive("date", dt), dt)
    }

}
