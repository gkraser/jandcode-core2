package jandcode.core.dbm.fixture

import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

class Fixture_Test extends Dbm_Test {

    @Test
    void test1() throws Exception {
        def fx = Fixture.create(model)
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
            tab1.add(a: i)
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
    void saveFixture1() throws Exception {
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
        ut.saveFixture(fx, true)
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
            ut.saveFixture(fx, true)
        }
        //
        def st = mdb.loadQuery("select * from tab1 where id>=50000 order by id")
        utils.outTable(st)
    }

}
