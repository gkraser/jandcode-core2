package jandcode.commons.collect;

import jandcode.commons.collect.*;
import jandcode.commons.test.*;
import jandcode.commons.variant.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HashMapNoCase_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        HashMapNoCase<Object> m = new HashMapNoCase<Object>();
        assertEquals(m.size(), 0);
        //
        m.put("Asd", 1);
        m.put("Dsa", 2);
        assertEquals(m.size(), 2);
        //
        assertEquals(m.containsKey("Asd"), true);
        assertEquals(m.containsKey("ASD"), true);
        //
        assertEquals(m.get("AsD"), 1);
        m.put("asD", 11);
        assertEquals(m.get("AsD"), 11);
        assertEquals(m.size(), 2);
        //
        m.remove("asd");
        assertEquals(m.size(), 1);
        //
    }

    @Test
    public void test_HashMapNoCase_putAll() throws Exception {
        HashMapNoCase<Object> m = new HashMapNoCase<>();
        //
        m.put("qwe", "1");
        m.put("asd", "2");
        m.put("zxc", "3");
        //
        Map<String, Object> ms = new HashMap<>();
        //
        ms.put("QWE", "11");
        ms.put("ASD", "22");
        //
        m.putAll(ms);
        //
        String s = m.toString();
        assertEquals(s, "{asd=22, zxc=3, qwe=11}");
    }

    @Test
    public void test_LinkedHashMapNoCase_putAll() throws Exception {
        LinkedHashMapNoCase<Object> m = new LinkedHashMapNoCase<>();
        //
        m.put("qwe", "1");
        m.put("asd", "2");
        m.put("zxc", "3");
        //
        Map<String, Object> ms = new HashMap<>();
        //
        ms.put("QWE", "11");
        ms.put("ASD", "22");
        //
        m.putAll(ms);
        //
        String s = m.toString();
        assertEquals(s, "{qwe=11, asd=22, zxc=3}");
    }

    @Test
    public void test_VariantMapNoCase_putAll() throws Exception {
        VariantMapNoCase m = new VariantMapNoCase();
        //
        m.put("qwe", "1");
        m.put("asd", "2");
        m.put("zxc", "3");
        //
        Map<String, Object> ms = new HashMap<>();
        //
        ms.put("QWE", "11");
        ms.put("ASD", "22");
        //
        m.putAll(ms);
        //
        String s = m.toString();
        assertEquals(s, "{qwe=11, asd=22, zxc=3}");
    }

}
