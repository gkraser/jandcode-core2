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
    public void test_work2_upd() throws Exception {
        mdb.createQuery("") // recreate db

        def svc = model.bean(GenIdService)
        //
        def g = svc.genIds.get("gtab3")

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

        def genNormal = svc.genIds.get("gtab1")
        updateCurrentId(genNormal, 0)
        assertEquals(genNormal.currentId, 1000)

        //
        def genCached = svc.getGenId("gtab1", 2)

        //logOn()

        assertEquals(genNormal.currentId, 1000)
        assertEquals(genCached.currentId, 1000)

        assertEquals(genNormal.nextId, 1001)
        assertEquals(genCached.nextId, 1003)

        assertEquals(genNormal.nextId, 1005)
        assertEquals(genCached.nextId, 1004)
        assertEquals(genCached.nextId, 1007)
        assertEquals(genNormal.nextId, 1009)
        assertEquals(genCached.nextId, 1008)
        assertEquals(genCached.nextId, 1011)

    }

    @Test
    public void test_work2_cached() throws Exception {
        def svc = model.bean(GenIdService)

        def genNormal = svc.genIds.get("gtab3").withMdb(mdb)
        updateCurrentId(genNormal, 0)
        assertEquals(genNormal.currentId, 1000)

        //
        def genCached = svc.getGenId("gtab3", 2).withMdb(mdb)

        //logOn()

        assertEquals(genNormal.currentId, 1000)
        assertEquals(genCached.currentId, 1000)

        assertEquals(genNormal.nextId, 1003)
        assertEquals(genCached.nextId, 1009)
        assertEquals(genNormal.nextId, 1015)
        assertEquals(genCached.nextId, 1012)
        assertEquals(genCached.nextId, 1021)
        assertEquals(genNormal.nextId, 1027)
        assertEquals(genCached.nextId, 1024)
        assertEquals(genCached.nextId, 1033)
    }

}
