package jandcode.core.dao;

import jandcode.core.dao.data.*;
import jandcode.core.dao.impl.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DaoClassDef_Test extends App_Test {

    public static class Dao2_override_normal extends Dao2 {

        public static void staticMethod1() {
        }

        public int sum(int a, int b) {
            return super.sum(a, b);
        }
    }

    public static class Dao2_override_bad extends Dao2 {
        public int sum(int a, int b, int c) {
            return super.sum(a, b);
        }
    }

    public static class Dao2_nopublic1 extends Dao2 {
        void sum3() {
        }
    }

    public static class Dao2_nopublic2 extends Dao2_nopublic1 {
        public void sum3() {
        }
    }

    void prnMethod(DaoMethodDef m) {
        System.out.println(m.getName() + ": " + m.getMethod());
    }

    void prnClass(DaoClassDef cls) {
        utils.delim(cls.getName());
        System.out.println(cls.getName() + ":");
        for (DaoMethodDef m : cls.getMethods()) {
            prnMethod(m);
        }
        utils.delim();
    }

    @Test
    public void groovy1() throws Exception {
        DaoClassDef cd = new DaoClassDefImpl(DaoGroovy1.class);
        prnClass(cd);
    }

    @Test
    public void override_normal1() throws Exception {
        DaoClassDef cd = new DaoClassDefImpl(Dao2_override_normal.class);
        prnClass(cd);
    }

    @Test
    public void override_bad1() throws Exception {
        DaoClassDef cd = null;
        try {
            cd = new DaoClassDefImpl(Dao2_override_bad.class);
            fail("Dao2_override_bad не должен создаваться");
        } catch (Exception e) {
            utils.showError(e);
        }
    }

    @Test
    public void nopublic1() throws Exception {
        DaoClassDef cd = null;
        try {
            cd = new DaoClassDefImpl(Dao2_nopublic1.class);
            fail("Dao2_nopublic1 не должен создаваться");
        } catch (Exception e) {
            utils.showError(e);
        }
    }

    @Test
    public void nopublic2() throws Exception {
        DaoClassDef cd = null;
        try {
            cd = new DaoClassDefImpl(Dao2_nopublic2.class);
            fail("Dao2_nopublic2 не должен создаваться");
        } catch (Exception e) {
            utils.showError(e);
        }
    }

    @Test
    public void param_name_java1() throws Exception {
        DaoClassDef cd = new DaoClassDefImpl(Dao2_override_normal.class);
        DaoMethodDef m = cd.getMethods().get("sum");
        assertEquals(m.getParams().size(), 2);
        assertEquals(m.getParams().get(1).getName(), "b");
        assertEquals(m.getParams().get(1).getIndex(), 1);
        assertEquals(m.getParams().get(1).getType(), int.class);
    }

    @Test
    public void param_name_groovy1() throws Exception {
        DaoClassDef cd = new DaoClassDefImpl(DaoGroovy2.class);
        DaoMethodDef m = cd.getMethods().get("sum");
        assertEquals(m.getParams().size(), 2);
        assertEquals(m.getParams().get(1).getName(), "b");
        assertEquals(m.getParams().get(1).getIndex(), 1);
        assertEquals(m.getParams().get(1).getType(), int.class);
    }


}
