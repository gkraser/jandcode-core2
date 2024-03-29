package jandcode.core.db.test

import jandcode.commons.*
import jandcode.commons.datetime.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

/**
 * Базовый класс для тестирования типов данных в базе.
 * Используется как предок при тестировании каждого драйвера для унификации поведения.
 *
 * В коде имеются проверки dbType. В этих местах имеются сильные отличия в поведении!
 */
abstract class DbDatatype_Test extends App_Test {

    DbSimpleTestSvc z = testSvc(DbSimpleTestSvc)

    ///////////////////

    @Test
    public void sqltypes() throws Exception {
        z.checkSqlTypes()
    }

    @Test
    public void storetypes() throws Exception {
        z.checkStoreTypes()
    }

    @Test
    public void nulltypes() throws Exception {
        z.checkNullTypes()
    }

    @Test
    public void test_long() throws Exception {
        assertEquals(z.dbdatatypeResult("long"), "long")
        //
        assertEquals(z.dbdatatypeRetrive("long", 123), 123)
        assertEquals(z.dbdatatypeRetrive("long", '1234'), 1234)
        assertEquals(z.dbdatatypeRetrive("long", null), 0)
    }

    @Test
    public void test_int() throws Exception {
        assertEquals(z.dbdatatypeResult("int"), "int")
        //
        assertEquals(z.dbdatatypeRetrive("int", 123), 123)
        assertEquals(z.dbdatatypeRetrive("int", '1234'), 1234)
        assertEquals(z.dbdatatypeRetrive("int", null), 0)
    }

    @Test
    public void test_smallint() throws Exception {
        assertEquals(z.dbdatatypeResult("smallint"), "smallint")
        //
        assertEquals(z.dbdatatypeRetrive("smallint", 123), 123)
        assertEquals(z.dbdatatypeRetrive("smallint", '1234'), 1234)
        assertEquals(z.dbdatatypeRetrive("smallint", null), 0)
    }

    @Test
    public void test_double() throws Exception {
        assertEquals(z.dbdatatypeResult("double"), "double")
        //
        double d
        Object ob

        d = z.dbdatatypeRetrive("double", (double) 123.456)
        assertEquals(d, (double) 123.456, 0.0001)

        d = z.dbdatatypeRetrive("double", '1234.678')
        assertEquals(d, (double) 1234.678, 0.0001)

        d = z.dbdatatypeRetrive("double", '1231234.67891')
        assertEquals(d, (double) 1231234.67891, 0.0001)

        ob = z.dbdatatypeRetrive("double", null)
        assertEquals(ob, (double) 0)
    }

    @Test
    public void test_string() throws Exception {
        assertEquals(z.dbdatatypeResult("string"), "string")
        //
        assertEquals(z.dbdatatypeRetrive("string", '123'), '123')
        assertEquals(z.dbdatatypeRetrive("string", 'Привет'), "Привет")
        assertEquals(z.dbdatatypeRetrive("string", 'Привет', 6), "Привет")
        assertEquals(z.dbdatatypeRetrive("string", null), '')
        assertEquals(z.dbdatatypeRetrive("string", 123), '123')
        //
        assertEquals(z.dbdatatypeRetrive("string", ""), "")
    }

    @Test
    public void test_memo() throws Exception {
        assertEquals(z.dbdatatypeResult("memo"), "memo")
        //
        assertEquals(z.dbdatatypeRetrive("memo", 'This is big string'), 'This is big string')
        assertEquals(z.dbdatatypeRetrive("memo", 'Это длинная строка'), "Это длинная строка")
        assertEquals(z.dbdatatypeRetrive("memo", ''), '')
        assertEquals(z.dbdatatypeRetrive("memo", null), '')
    }

    @Test
    public void test_blob() throws Exception {
        assertEquals(z.dbdatatypeResult("blob"), "blob")
        //

        byte[] a = [1, 2, 3, 4, 5, 6, 7, 8, 9]
        byte[] a2 = [1, 2, 3, 4, 5, 6, 7, 8, 9]
        assertArrayEquals(z.dbdatatypeRetrive("blob", a), a2)

        byte[] b = []
        byte[] c = []
        assertArrayEquals(z.dbdatatypeRetrive("blob", b), c)

        assertArrayEquals(z.dbdatatypeRetrive("blob", null), c)
    }

    @Test
    public void test_boolean() throws Exception {
        assertEquals(z.dbdatatypeResult("boolean"), "smallint")
        //
        assertEquals(z.dbdatatypeRetrive("boolean", 1), 1)
        assertEquals(z.dbdatatypeRetrive("boolean", '1'), 1)
        assertEquals(z.dbdatatypeRetrive("boolean", null), 0)
    }

    @Test
    public void test_date() throws Exception {
        if (z.dbType == "oracle") {
            assertEquals(z.dbdatatypeResult("date"), "datetime")
        } else {
            assertEquals(z.dbdatatypeResult("date"), "date")
        }
        //
        XDateTime dt;

        dt = XDateTime.create("2012-11-30")
        assertEquals(z.dbdatatypeRetrive("date", dt), dt)

        dt = XDateTime.create("2012-11-30T22:23:24")
        if (z.dbType == "oracle") {
            assertEquals(z.dbdatatypeRetrive("date", dt), dt)
        } else {
            assertEquals(z.dbdatatypeRetrive("date", dt), dt.clearTime())
        }

        dt = XDateTime.create("2012-11-30T22:23:24.123")
        if (z.dbType == "oracle") {
            assertEquals(z.dbdatatypeRetrive("date", dt), dt.clearMSec())
        } else {
            assertEquals(z.dbdatatypeRetrive("date", dt), dt.clearTime())
        }

        dt = XDateTime.create("1984-04-01")
        if (z.dbType == "oracle") {
            assertEquals(z.dbdatatypeRetrive("date", dt), XDateTime.create("1984-04-01T01:00:00"))
        } else {
            assertEquals(z.dbdatatypeRetrive("date", dt), dt)
        }

        assertEquals(z.dbdatatypeRetrive("date", null), UtDateTime.EMPTY_DATE)
    }

    @Test
    public void test_datetime() throws Exception {
        assertEquals(z.dbdatatypeResult("datetime"), "datetime")
        //
        XDateTime dt;

        dt = XDateTime.create("2012-11-30")
        assertEquals(z.dbdatatypeRetrive("datetime", dt), dt)

        dt = XDateTime.create("2012-11-30T22:23:24")
        assertEquals(z.dbdatatypeRetrive("datetime", dt), dt)

        dt = XDateTime.create("2012-11-30T22:23:24.123")
        assertEquals(z.dbdatatypeRetrive("datetime", dt), dt.clearMSec())

        dt = XDateTime.create("1984-04-01")
        assertEquals(z.dbdatatypeRetrive("datetime", dt), XDateTime.create("1984-04-01T01:00:00"))

        dt = XDateTime.create("1984-04-01T15:16:17")
        assertEquals(z.dbdatatypeRetrive("datetime", dt), dt)

        assertEquals(z.dbdatatypeRetrive("date", null), UtDateTime.EMPTY_DATE)
    }


}
