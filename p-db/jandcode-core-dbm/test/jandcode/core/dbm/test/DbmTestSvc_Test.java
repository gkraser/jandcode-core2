package jandcode.core.dbm.test;

import jandcode.core.store.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbmTestSvc_Test extends Dbm_Test {

    @Test
    public void test1() throws Exception {
        String s = dbm.getModel().getName();
        assertEquals(s, "testdb");
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
