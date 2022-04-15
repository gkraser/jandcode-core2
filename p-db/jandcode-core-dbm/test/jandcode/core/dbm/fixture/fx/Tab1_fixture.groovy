package jandcode.core.dbm.fixture.fx

import jandcode.core.dbm.fixture.*

class Tab1_fixture extends BaseFixtureBuilder {

    long startId = 20000
    long count = 10

    protected void onBuild() {
        def tab1 = fx.table("tab1")

        long id = startId
        for (i in 1..count) {
            tab1.add(id: id, a: id * 10)
            id++
        }
    }

}
