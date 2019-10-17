package jandcode.core.db;

import jandcode.commons.test.*;
import jandcode.commons.variant.*;
import jandcode.core.db.impl.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbSourceProps_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        DbSourceProps p = new DbSourcePropsImpl();
        //

        p.setValue("p2", "2-${p1}");
        p.setValue("p3", "3-${p2}");
        p.setValue("p1", "Q1");

        p.setValue("p4", "Q4-${p5}");
        p.setValue("p5", "Q5-${p4}");

        p.setValue("p6", "Q6-${p7}");
        p.setValue("p7", "Q7-${p8}");
        p.setValue("p8", "Q8-${p6}");

        assertEquals(p.getString("p1"), "Q1");
        assertEquals(p.getString("p2"), "2-Q1");
        assertEquals(p.getString("p3"), "3-2-Q1");
        assertEquals(p.getString("p4"), "Q4-Q5-");
        assertEquals(p.getString("p5"), "Q5-Q4-");
        assertEquals(p.getString("p6"), "Q6-Q7-Q8-");
        assertEquals(p.getString("p7"), "Q7-Q8-Q6-");
        assertEquals(p.getString("p8"), "Q8-Q6-Q7-");

        utils.outMap(p);

    }

    @Test
    public void getMap1() throws Exception {
        DbSourceProps p = new DbSourcePropsImpl();
        //
        p.setValue("url", "a://${database}/${username}");
        p.setValue("database", "d${var1}");
        p.setValue("username", "u${var1}");
        p.setValue("var1", "1");
        p.setValue("system.database", "dsys${var1}");
        p.setValue("system.username", "usys${var1}");

        assertEquals(p.getString("database"), "d1");
        assertEquals(p.getString("url"), "a://d1/u1");

        IVariantMap p1;

        //
        p1 = p.subMap("system", false);
        assertEquals(p1.size(), 2);
        assertEquals(p1.getString("database"), "dsys1");

        //
        p1 = p.subMap("system", true);
        assertEquals(p1.size(), 2);
        assertEquals(p1.getString("database"), "dsys${var1}");

    }


}
