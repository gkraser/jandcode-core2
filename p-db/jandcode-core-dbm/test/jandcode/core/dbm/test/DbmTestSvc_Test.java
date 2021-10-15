package jandcode.core.dbm.test;

import jandcode.core.store.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbmTestSvc_Test extends App_Test {

    DbmTestSvc dbm = testSvc(DbmTestSvc.class);

    @Test
    public void test1() throws Exception {
        String s = dbm.getModel().getName();
        assertEquals(s, "test1");
    }

    @Test
    public void storeStruct() throws Exception {
        Store st = dbm.getMdb().createStore();
        st.addField("id", "long");
        st.addField("dict1", "long").setDict("dict1");
        st.addField("dict2", "long").setDict("dict2");
        st.addField("dict22", "string").setDict("dict2");
        st.addField("nodict1", "string");
        //
        utils.outTable(dbm.createStoreStruct(st));
    }


}
