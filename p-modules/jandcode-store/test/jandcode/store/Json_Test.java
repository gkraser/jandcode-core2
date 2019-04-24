package jandcode.store;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Json_Test extends App_Test {


    @Test
    public void test1() throws Exception {
        StoreService svc = app.bean(StoreService.class);
        //
        Store st = svc.createStore();
        st.addField("id", "long");
        st.addField("name", "string");
        st.addField("dt", "date");
        st.addField("null1", "string");
        //
        XDateTime d = UtDateTime.create("2012-11-30");
        for (int i = 1; i <= 2; i++) {
            StoreRecord r = st.add();
            r.setValue("id", i);
            r.setValue("name", "nm-" + i);
            r.setValue("dt", d.addDays(-i));
        }

        //
        String s;

        s = UtJson.toJson(st.get(0));
        assertEquals(s, "{\"id\":1,\"name\":\"nm-1\",\"dt\":\"2012-11-29\"}");

        s = UtJson.toJson(st);
        assertEquals(s, "[{\"id\":1,\"name\":\"nm-1\",\"dt\":\"2012-11-29\"},{\"id\":2,\"name\":\"nm-2\",\"dt\":\"2012-11-28\"}]");
    }


}
