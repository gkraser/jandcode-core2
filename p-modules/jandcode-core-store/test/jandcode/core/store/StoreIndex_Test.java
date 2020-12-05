package jandcode.core.store;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StoreIndex_Test extends App_Test {

    StoreService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(StoreService.class);
    }

    Store createSt1() {
        Store st = svc.createStore();
        st.addField("id", "long");
        st.addField("txt", "string");
        //
        for (int i = 1; i < 100; i++) {
            StoreRecord r = st.add();
            r.setValue("id", i);
            if (i == 20) {
                r.setValue("id", null);
            }
            r.setValue("txt", -i);
            if (i == 21) {
                r.setValue("txt", null);
            }
        }
        return st;
    }

    @Test
    public void test1() throws Exception {
        Store st = createSt1();
        //
        assertTrue(st.size() > 90);
        assertNull(st.getById(6666));
        assertEquals(st.getById(55).getValue("id"), 55L);
        assertEquals(st.getBy("txt", -55).getValue("id"), 55L);
        assertNull(st.getBy("txt", 6666));
        assertNull(st.getById(null));
        //
    }

    @Test
    public void auto_add() throws Exception {
        Store st = createSt1();
        StoreIndex idx = st.getIndex("id");
        int sz = st.size();
        StoreRecord rec = idx.get(5555);
        assertNull(rec);
        assertEquals(sz, st.size());

        rec = idx.get(5555, true);
        assertEquals(sz + 1, st.size());
        assertEquals(rec.getValue("id"), 5555L);
    }


}
