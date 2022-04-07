package jandcode.core.dbm.verdb;

import jandcode.core.dbm.test.*;
import jandcode.core.store.*;
import org.junit.jupiter.api.*;

public class VerdbProcessor_Test extends Dbm_Test {

    private VerdbProcessor createProcessor() {
        VerdbService svc = getModel().bean(VerdbService.class);
        VerdbModule mod = svc.getVerdbModules().get("dir2").createInst();
        return new VerdbProcessor(mod, getModel().getDbSource());
    }

    private Store outVerdb() throws Exception {
        Store st = getMdb().loadQuery("select * from verdb_info order by module_name");
        utils.outTable(st);
        return st;
    }

    @Test
    public void init_not_exist() throws Exception {
        dbm.dropDb();
        //
        VerdbProcessor p = createProcessor();
        try {
            p.init(true);
        } finally {
            p.done();
        }
        //
        outVerdb();
    }

    @Test
    public void upgrade() throws Exception {
        dbm.dropDb();
        //
        utils.logOn();
        VerdbProcessor p = createProcessor();
        try {
            p.init(true);
            p.upgrade(null);
        } finally {
            p.done();
        }
        //
        outVerdb();
    }


}
