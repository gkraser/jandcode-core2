package jandcode.store;

import jandcode.core.test.*;
import jandcode.store.std.*;
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
        System.out.println(svc.getFieldTypes());
    }

    @Test
    public void create1() throws Exception {
        Store st = svc.createStore();
        st.addField("id", "long");
        st.addField("name", "string");
        st.addField("code", "string", 10);
        //
        assertEquals(st.getCountFields(), 3);
        assertTrue(st.getField("name") instanceof StringStoreField);
        assertEquals(st.getField("name").getSize(), 0);
        assertEquals(st.getField("code").getSize(), 10);
        assertEquals(st.getField("code").getIndex(), 2);
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
