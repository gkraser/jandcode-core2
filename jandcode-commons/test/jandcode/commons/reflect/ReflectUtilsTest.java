package jandcode.commons.reflect;

import jandcode.commons.reflect.impl.*;
import jandcode.commons.conf.*;
import jandcode.commons.conf.impl.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectUtilsTest extends Utils_Test {

    static Object res;
    static Map<String, Object> resMap = new LinkedHashMap<>();

    public static class Z1 {
        public void setStr(String s) {
            res = s;
        }

        public void setDomName(String s) {
            res = s;
        }

        private String prop1;

        public String getProp1() {
            return prop1;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }
    }

    public static class Z2 extends Z1 implements IReflectUnknownSetter {

        public void onUnknownSetter(String name, Object value) {
            resMap.put(name, value);
        }

    }

    public void setUp() throws Exception {
        super.setUp();
        //
        res = null;
        resMap = new LinkedHashMap<>();
    }

    @Test
    public void test1() throws Exception {
        ReflectUtils svc = new ReflectUtilsImpl();
        Z1 inst = new Z1();
        //
        ReflectClazz cz = svc.getClazz(inst.getClass());
        cz.invokeSetter(inst, "str", "qaz");
        assertEquals(res, "qaz");
        //
    }

    @Test
    public void testByName() throws Exception {
        ReflectUtils svc = new ReflectUtilsImpl();
        Z1 inst = new Z1();
        //
        ReflectClazz cz = svc.getClazz(inst.getClass());
        cz.invokeSetter(inst, "dom", "qaz1");
        assertEquals(res, "qaz1");
        //
    }

    @Test
    public void tesList1() throws Exception {
        ReflectUtils svc = new ReflectUtilsImpl();
        for (ReflectClazz z : svc.list(getClass().getPackage().getName(), false)) {
            System.out.println(z.getCls());
        }
    }

    @Test
    public void test_prop1() throws Exception {
        ReflectUtils svc = new ReflectUtilsImpl();
        Z1 inst = new Z1();
        //
        ReflectClazz cz = svc.getClazz(inst.getClass());
        cz.invokeSetter(inst, "prop1", "s1");
        assertEquals(inst.getProp1(), "s1");
        assertEquals(cz.invokeGetter(inst, "prop1"), "s1");
        //
    }

    @Test
    public void unknownSetter1() throws Exception {
        ReflectUtils svc = new ReflectUtilsImpl();
        Z2 inst = new Z2();
        //
        ReflectClazz cz = svc.getClazz(inst.getClass());
        cz.invokeSetter(inst, "q1", 1);
        cz.invokeSetter(inst, "q2", "v2");
        cz.invokeSetter(inst, "str", "qaz");
        assertEquals(resMap.toString(), "{q1=1, q2=v2}");
        assertEquals(res, "qaz");
        //
    }

}
