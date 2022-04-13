package jandcode.core.dbm.fixture

import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

class Fixture_Test extends Dbm_Test {

    @Test
    public void test1() throws Exception {
        def fx = Fixture.create(model)
        //
        fx.table('tab1').add(a: 1, b: 11)
        fx.table('tab1').add(a: 2, b: 22)
        fx.table('tab2').add(a: 11, b: 111)
        //
        utils.outTableList(fx.stores)
    }

    @Test
    public void real_1() throws Exception {
        def fx = Fixture.create(model)

        def tab1 = fx.table("tab1")
        def tab2 = fx.table("tab2")

        for (i in 1..5) {
            tab1.add(a: i)
        }

        utils.outTableList(fx.stores)
    }

    @Test
    public void loadFromFile1() throws Exception {
        def fx = Fixture.create(model)
        fx.table("tab1").loadFromFile(utils.getTestFile("data/tab1.csv"))

        utils.outTableList(fx.stores)
    }

    @Test
    public void loadFromPath1() throws Exception {
        def fx = Fixture.create(model)
        fx.loadFromPath(utils.getTestFile("data"))

        utils.outTableList(fx.stores)
    }


}
