package jandcode.core.dbm.genid.postgresql

import jandcode.core.dbm.genid.*
import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Postgresql_GenId_Test extends Dbm_Test {

    @Test
    public void test1() throws Exception {
        dbm.showDb()
        def st = mdb.loadQuery("select * from gtab1")
        utils.outTable(st)
    }


    /**
     * Обновить currentId в базе
     */
    void updateCurrentId(GenId g, long value) {
        def svc = model.bean(GenIdService)
        svc.updateCurrentId(g.name, value)
    }

    @Test
    public void test_ddl() throws Exception {
        println dbm.grabCreateSql()
    }

    @Test
    public void test_create() throws Exception {
        dbm.dropDb()
        mdb.loadQuery("select 1")
    }

    @Test
    public void pg_seq() throws Exception {
        def st = mdb.loadQuery("select cycle from pg_sequences")
        utils.outTable(st)
    }


    @Test
    public void test_work1() throws Exception {
        def svc = model.bean(GenIdService)
        //
        for (i in 1..3) {
            long a = svc.getGenId("gtab1").nextId
            println a
        }
        println "last=" + svc.genIds.get("gtab1").currentId
    }

    @Test
    public void test_work2() throws Exception {
        def svc = model.bean(GenIdService)
        //
        for (i in 1..3) {
            long a = svc.getGenId("tab1").nextId
            println a
        }
        println "last=" + svc.genIds.get("tab2").currentId
    }

    @Test
    public void test_work2_upd() throws Exception {
        def svc = model.bean(GenIdService)
        //
        def g = svc.genIds.get("tab2")

        updateCurrentId(g, 0)
        assertEquals(g.currentId, 1000)
        assertEquals(g.nextId, 1003)

        updateCurrentId(g, 1006)
        assertEquals(g.currentId, 1006)
        assertEquals(g.nextId, 1009)

        updateCurrentId(g, 1007)
        assertEquals(g.currentId, 1009)
        assertEquals(g.nextId, 1012)

        updateCurrentId(g, 1008)
        assertEquals(g.currentId, 1009)
        assertEquals(g.nextId, 1012)

        updateCurrentId(g, 1009)
        assertEquals(g.currentId, 1009)
        assertEquals(g.nextId, 1012)

        updateCurrentId(g, 1010)
        assertEquals(g.currentId, 1012)
        assertEquals(g.nextId, 1015)
    }

    @Test
    public void test_work1_cached() throws Exception {
        def svc = model.bean(GenIdService)

        def genNormal = svc.genIds.get("tab1")
        updateCurrentId(genNormal, 0)
        assertEquals(genNormal.currentId, 1000)

        //
        def genCached = svc.getGenId("tab1", 2)

        //logOn()

        assertEquals(genNormal.currentId, 1000)
        assertEquals(genCached.nextId, 1001)
        assertEquals(genNormal.currentId, 1002)
        assertEquals(genNormal.nextId, 1003)

        assertEquals(genCached.nextId, 1002)
        assertEquals(genCached.nextId, 1004)
        assertEquals(genCached.nextId, 1005)
        assertEquals(genCached.nextId, 1006)
    }

    @Test
    public void test_work2_cached() throws Exception {
        def svc = model.bean(GenIdService)

        def genNormal = svc.genIds.get("tab2")
        updateCurrentId(genNormal, 0)
        assertEquals(genNormal.currentId, 1000)

        //
        def genCached = svc.getGenId("tab2", 2)

        //logOn()

        assertEquals(genNormal.currentId, 1000)
        assertEquals(genCached.nextId, 1003)
        assertEquals(genNormal.currentId, 1006)
        assertEquals(genNormal.nextId, 1009)

        assertEquals(genCached.nextId, 1006)
        assertEquals(genCached.nextId, 1012)
        assertEquals(genCached.nextId, 1015)
        assertEquals(genCached.nextId, 1018)
    }

}
