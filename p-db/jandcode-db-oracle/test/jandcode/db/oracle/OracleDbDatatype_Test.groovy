package jandcode.db.oracle

import jandcode.db.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

//@Ignore
class OracleDbDatatype_Test extends JdbcDbDatatypeTestCase {

    @Test
    public void test1() throws Exception {
        byte[] a = [1, 2, 3, 4, 5, 6, 7, 8, 9]
        assertArrayEquals(z.dbdatatypeRetrive("blob", a), a)
    }

}
