package jandcode.core.store;

import jandcode.core.store.impl.*;
import jandcode.core.store.std.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StoreService_Test extends App_Test {

    StoreService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(StoreService.class);
    }

    @Test
    public void types() throws Exception {
        System.out.println(svc.getStoreDataTypes());
    }

    @Test
    public void create1() throws Exception {
        Store st = svc.createStore();
        st.addField("id", "long");
        st.addField("name", "string");
        st.addField("code", "string", 10);
        //
        assertEquals(st.getCountFields(), 3);
        assertTrue(st.getField("name") instanceof DefaultStoreField);
        assertEquals(st.getField("name").getSize(), 0);
        assertEquals(st.getField("code").getSize(), 10);
        assertEquals(st.getField("code").getIndex(), 2);
        assertTrue(st.getField("code").getStoreDataType() instanceof StoreDataType_string);
    }

    @Test
    public void removeField1() throws Exception {
        Store st = svc.createStore();
        st.addField("id", "long");
        st.addField("name", "string");
        st.addField("code", "string", 10);
        //
        assertEquals(st.getField("id").getIndex(), 0);
        assertEquals(st.getField("code").getIndex(), 2);
        st.removeField("name");
        assertEquals(st.getField("id").getIndex(), 0);
        assertEquals(st.getField("code").getIndex(), 1);
    }


}
