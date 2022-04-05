package jandcode.core.dbm.verdb;

import jandcode.core.dbm.test.*;
import jandcode.core.store.*;
import org.junit.jupiter.api.*;

public class VerdbOper_Test extends Dbm_Test {

    @Test
    public void test1() throws Exception {
        dbm.dropDbTable("tab1");
        dbm.dropDbTable("tab2");
        //
        VerdbModule mod = getModel().bean(VerdbService.class).getVerdbModules().get("dir2").createInst();
        //
        for (VerdbDir dir : mod.getDirs()) {
            for (VerdbFile file : dir.getFiles()) {
                for (VerdbOper oper : file.getOpers()) {
                    oper.exec(getMdb());
                }
            }
        }
        //
        Store st = getMdb().loadQuery("select * from tab1");
        utils.outTable(st);
        st = getMdb().loadQuery("select * from tab2");
        utils.outTable(st);
    }


}
