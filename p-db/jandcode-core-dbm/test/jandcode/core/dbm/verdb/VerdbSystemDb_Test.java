package jandcode.core.dbm.verdb;

import jandcode.core.dbm.test.*;
import jandcode.core.dbm.verdb.impl.*;
import jandcode.core.store.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class VerdbSystemDb_Test extends Dbm_Test {

    {
        //utils.logSetUp = true;
    }

    private void dropSys() {
        try {
            getMdb().execQueryNative("drop table verdb_info");
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * проверяем на чистой базе, verdb_info нет
     */
    @Test
    public void test1() throws Exception {
        dropSys();
        VerdbSystemDb vdb = new VerdbSystemDb(getMdb(), "module1");
        VerdbVersion v;
        //
        v = vdb.loadVersion();
        assertEquals(v.getText(), "0.0.0");
        //
        vdb.saveVersion(VerdbVersion.create("1.2.3"));
        v = vdb.loadVersion();
        assertEquals(v.getText(), "1.2.3");
        //
        Store st = getMdb().loadQuery("select * from verdb_info");
        utils.outTable(st);
        //
        part2();
        part3();
    }

    /**
     * проверяем второй модль в существующей таблице
     */
    private void part2() throws Exception {
        VerdbSystemDb vdb = new VerdbSystemDb(getMdb(), "module2");
        vdb.install();

        VerdbVersion v;
        //
        v = vdb.loadVersion();
        assertEquals(v.getText(), "0.0.0");
        //
        vdb.saveVersion(VerdbVersion.create("2.3.4"));
        v = vdb.loadVersion();
        assertEquals(v.getText(), "2.3.4");
        //
        Store st = getMdb().loadQuery("select * from verdb_info");
        utils.outTable(st);
    }

    /**
     * проверяем существующий модуль
     */
    private void part3() throws Exception {
        VerdbSystemDb vdb = new VerdbSystemDb(getMdb(), "module2");
        vdb.install();

        VerdbVersion v;
        //
        v = vdb.loadVersion();
        assertEquals(v.getText(), "2.3.4");
        //
        vdb.saveVersion(VerdbVersion.create("2.3.5"));
        v = vdb.loadVersion();
        assertEquals(v.getText(), "2.3.5");
        //
        Store st = getMdb().loadQuery("select * from verdb_info");
        utils.outTable(st);
    }


}
