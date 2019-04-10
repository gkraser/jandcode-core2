package jandcode.commons.json;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtJson_Test extends Utils_Test {

    protected void cnv(Object data) {
        utils.delim("");
        System.out.println("cnv: " + data);
        String s = UtJson.toJson(data);
        Object r = UtJson.fromJson(s);
        System.out.println("obj: " + r);
        System.out.println("jsn: " + s);
        if (r != null) {
            System.out.println("cls: " + r.getClass());
        }
    }

    @Test
    public void test1() throws Exception {

        cnv(UtCnv.toMap(1, 2, 2, 3, "ффф", "cc", "z", UtCnv.toMap(1, 2, 2, 3)));

        cnv(Arrays.asList(UtDateTime.today(), UtDateTime.now().clearMSec(),
                UtCnv.toMap(1, UtDateTime.today(), 2, UtDateTime.now())));

    }

    @Test
    public void test_num() throws Exception {
        List a = Arrays.asList(1, 2.0, 3);
        assertEquals(UtJson.toJson(a), "[1,2.0,3]");
        List b = (List) UtJson.fromJson("[1,2.0,3]");
        //println b
        assertEquals(b.get(0).getClass(), Long.class);
        assertEquals(b.get(1).getClass(), Double.class);
        assertEquals(b.get(2).getClass(), Long.class);
    }

}