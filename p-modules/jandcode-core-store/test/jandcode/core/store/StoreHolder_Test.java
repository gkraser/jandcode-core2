package jandcode.core.store;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StoreHolder_Test extends App_Test {

    StoreService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(StoreService.class);
    }

    @Test
    public void test1() throws Exception {
        StoreHolder h = StoreHolder.create();

        Store st = svc.createStore();
        st.setName("a1");
        h.add(st);

        Store st2 = svc.createStore();
        st2.setName("a1");
        h.add("a2", st2);

        assertEquals(h.getItems().size(), 2);

        StringBuilder sb = new StringBuilder();
        for (Store z : h) {
            sb.append(z.getName()).append(':');
        }
        assertEquals(sb.toString(), "a1:a2:");
    }


}
