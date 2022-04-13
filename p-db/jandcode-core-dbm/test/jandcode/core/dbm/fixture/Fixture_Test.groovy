package jandcode.core.dbm.fixture

import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

class Fixture_Test extends Dbm_Test {

    void outFixture(Fixture fx, int limit = -1) {
        for (st in fx.stores) {
            println ""
            println st.name
            utils.outTable(st, limit)
        }
    }

    @Test
    public void test1() throws Exception {
        def fx = Fixture.create(model)
        //
        fx.table('tab1').add(a: 1, b: 11)
        fx.table('tab1').add(a: 2, b: 22)
        fx.table('tab2').add(a: 11, b: 111)
        //
        outFixture(fx)
    }


}
