package jandcode.commons;

import jandcode.commons.named.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NamedList_Test extends Utils_Test {


    class LST extends DefaultNamedList<Named> {

        LST() {
        }

        LST(String names) {
            a(names);
        }

        public void a(String nms) {
            if (UtString.empty(nms)) {
                return;
            }
            String[] a = nms.split(",");
            for (String s : a) {
                Named b = new Named();
                b.setName(s);
                add(b);
            }
        }

        List getList() {
            return list;
        }

        Map getMap() {
            return map;
        }
    }

    private void checkSize(LST z) {
        assertEquals(z.getList().size(), z.getMap().size());
    }

    private void checkOrd(NamedList<Named> z, String names) {
        String s = "";
        for (Named named : z) {
            if (s.length() != 0) {
                s = s + ",";
            }
            s = s + named.getName();
        }
        assertEquals(s, names);
    }

    @Test
    public void test1() throws Exception {
        Named o1 = new Named();
        o1.setName("n1");
        Named o2 = new Named();
        o2.setName("N1");
        Named o3 = new Named();
        o3.setName("N1_");
        Named o4 = new Named();
        o4.setName("N2");

        LST z = new LST();
        z.add(o1);
        z.add(o2);
        z.add(o4);

        assertEquals(z.size(), 2);

        assertTrue(z.contains("n1"));
        assertTrue(z.contains("N1"));
        assertTrue(!z.contains("n2_"));
        assertTrue(z.contains("n2"));

        assertTrue(!z.contains(o1));
        assertTrue(z.contains(o2));
        assertTrue(!z.contains(o3));
        assertTrue(z.contains(o4));

        checkSize(z);
        checkOrd(z, "N1,N2");
    }


    @Test
    public void testDup() throws Exception {
        LST z = new LST("a,b,c,d,b,d");
        assertEquals(z.size(), 4);
    }

    @Test
    public void testEm() throws Exception {
        LST z = new LST("");
        assertEquals(z.size(), 0);
        Named a = new Named();
        try {
            z.add(a);
            fail();
        } catch (Throwable e) {
        }
    }

    @Test
    public void testGet() throws Exception {
        LST z = new LST("a,b,c");
        assertEquals(z.get("a").getName(), "a");
        assertEquals(z.find("a_"), null);
        try {
            z.get("a_");
            fail();
        } catch (Exception e) {
        }
    }

}
