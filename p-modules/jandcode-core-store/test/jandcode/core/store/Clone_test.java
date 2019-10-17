package jandcode.core.store;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Clone_test extends App_Test implements Cloneable {

    @Test
    public void test1() throws Exception {
        StoreService svc = app.bean(StoreService.class);
        //
        Store st1 = svc.createStore();
        st1.addField("id", "long");
        st1.addField("name", "string");
        st1.addField("rawname", "rawstring");

        for (int i = 1; i <= 2; i++) {
            StoreRecord r = st1.add();
            r.setValue("id", i);
            r.setValue("name", "  name-" + i + "                ");
            r.setValue("rawname", "  rawname-" + i + "  ");
        }

        //
        Store st2 = st1.cloneStore();

        utils.outTable(st1);

        for (int i = 1; i <= 3; i++) {
            StoreRecord r = st2.add();
            r.setValue("id", i * 10);
            r.setValue("name", "  z-name-" + i + "                ");
            r.setValue("rawname", "  z-rawname-" + i + "  ");
        }
        utils.outTable(st2);

        assertNotSame(st1, st2);
        assertEquals(st1.getClass(), st2.getClass());

        assertNotSame(st1.getField(0), st2.getField(0));
        assertNotSame(st1.getRecords(), st2.getRecords());
    }


}
