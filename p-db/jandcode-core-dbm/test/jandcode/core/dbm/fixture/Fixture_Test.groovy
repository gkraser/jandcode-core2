package jandcode.core.dbm.fixture

import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

class Fixture_Test extends Dbm_Test {

    @Test
    void test1() throws Exception {
        def fx = Fixture.create(model)
        fx.table('tab1').rangeId(1)
        fx.table('tab2').rangeId(2)
        //
        fx.table('tab1').add(a: 1, b: 11)
        fx.table('tab1').add(a: 2, b: 22)
        fx.table('tab2').add(a: 11, b: 111)
        //
        utils.outTableList(fx.stores)
    }

    @Test
    void real_1() throws Exception {
        def fx = Fixture.create(model)

        def tab1 = fx.table("tab1")
        def tab2 = fx.table("tab2")

        for (i in 1..5) {
            tab1.add(id: i, a: i)
        }

        utils.outTableList(fx.stores)
    }

    @Test
    void loadFromFile1() throws Exception {
        def fx = Fixture.create(model)
        fx.table("tab1").loadFromFile(utils.getTestFile("data/tab1.csv"))

        utils.outTableList(fx.stores)
    }

    @Test
    void loadFromPath1() throws Exception {
        def fx = Fixture.create(model)
        fx.loadFromPath(utils.getTestFile("data"))

        utils.outTableList(fx.stores)

        for (tb in fx.tables) {
            println "${tb.name} ${tb.rangeId}"
        }
    }

    @Test
    void updateFixture1() throws Exception {
        mdb.createQuery("")

        def fx = Fixture.create(model)

        def tab1 = fx.table("tab1")
        def tab2 = fx.table("tab2")

        for (i in 1..50000) {
            tab1.add(id: i, a: i * 10)
        }

        for (i in 1..60000) {
            tab2.add(id: i, b: i * 10)
        }

        //utils.outTableList(fx.stores, 3)

        //
        FixtureMdbUtils ut = new FixtureMdbUtils(mdb)
        ut.updateFixture(fx, true)
    }


    @Test
    public void suite1() throws Exception {
        def svc = app.bean(FixtureService)
        //
        def suite = svc.createFixtureSuite("tab1")
        def bs = suite.createBuilders()
        //
        FixtureMdbUtils ut = new FixtureMdbUtils(mdb)
        for (b in bs) {
            def fx = b.build(model)
            utils.outTableList(fx.stores)
            ut.updateFixture(fx, true)
        }
        //
        def st = mdb.loadQuery("select * from tab1 where id>=50000 order by id")
        utils.outTable(st)
    }

    @Test
    public void suite1_1() throws Exception {
        def svc = app.bean(FixtureService)
        //
        def suite = svc.createFixtureSuite("tab1.1")
        def bs = suite.createBuilders()
        //
        FixtureMdbUtils ut = new FixtureMdbUtils(mdb)
        for (b in bs) {
            def fx = b.build(model)
            utils.outTableList(fx.stores)
            ut.updateFixture(fx, true)
        }
        //
        def st = mdb.loadQuery("select * from tab1 where id>=50000 order by id")
        utils.outTable(st)
    }

    void checkRange(FixtureRangeId r, long startId, long endId) {
        assertEquals(r.startId, startId, "range startId")
        assertEquals(r.endId, endId, "range endId")
    }

    @Test
    public void genid() throws Exception {
        def fx = Fixture.create(model)

        def tab1 = fx.table("tab1")
        checkRange(tab1.rangeId, 0, 0)

        tab1.add(id: 3)
        checkRange(tab1.rangeId, 3, 3)

        tab1.rangeId(2)
        checkRange(tab1.rangeId, 2, 3)

        tab1.rangeId(2, 30)
        checkRange(tab1.rangeId, 2, 30)

        //
        assertEquals(tab1.nextId, 2)
        assertEquals(tab1.nextId, 3)
        assertEquals(tab1.lastId, 3)
    }


    @Test
    public void maxId1() throws Exception {
        def fx = Fixture.create(model)

        def tab1 = fx.table("tab1")

        assertEquals(tab1.maxIdInRange, 0)

        tab1.add(id: 220)
        tab1.add(id: 420)
        assertEquals(tab1.maxIdInRange, 420)

        tab1.rangeId(100, 150)
        assertEquals(tab1.maxIdInRange, 100)

        tab1.rangeId(200, 300)
        assertEquals(tab1.maxIdInRange, 220)
    }

}
