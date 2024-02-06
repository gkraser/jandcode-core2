package jandcode.core.dao;

import jandcode.core.dao.data.*;
import jandcode.core.dao.data_bad.*;
import jandcode.core.dao.impl.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DaoInvoker_Test extends App_Test {

    @Test
    public void test1() throws Exception {

        DaoInvoker m = app.create(DefaultDaoInvoker.class);
        DaoClassDef c = new DaoClassDefImpl(Dao2.class);

        Object res = m.invokeDao(c.getMethods().get("sum2"), 2, 4);
        System.out.println("res2=" + res);

        res = m.invokeDao(c.getMethods().get("sum"), 2, 4);
        System.out.println("res=" + res);

    }

    @Test
    public void test11() throws Exception {

        DaoInvoker m = app.create(DefaultDaoInvoker.class);
        DaoClassDef c = app.bean(DaoService.class).getDaoClassDef(Dao2.class);

        Object res = m.invokeDao(c.getMethods().get("sum2"), 2, 4);
        System.out.println("res2=" + res);

        res = m.invokeDao(c.getMethods().get("sum"), 2, 4);
        System.out.println("res=" + res);

    }

    @Test
    public void test2() throws Exception {

        DaoInvoker m = app.create(DefaultDaoInvoker.class);

        Dao2 z = m.createDao(Dao2.class);

        int res = z.sum2(2, 4);
        System.out.println("res2=" + res);

        res = z.sum(2, 4);
        System.out.println("res=" + res);

        try {
            z.getContext();
            fail();
        } catch (Exception e) {
            System.out.println("Только dao методы можно вызывать вне процесса исполнения dao");
            System.out.println("context не существут вне вызова dao");
            utils.showError(e);
        }

    }

    @Test
    public void groovy1() throws Exception {
        DaoInvoker m = app.create(DefaultDaoInvoker.class);
        DaoGroovy1 z = m.createDao(DaoGroovy1.class);
        z.dummy1();
    }

    @Test
    public void isDao2() throws Exception {
        DaoInvoker m = app.create(DefaultDaoInvoker.class);
        //
        Dao1 z1 = m.createDao(Dao1.class);
        assertFalse(z1.isDao2());
        //
        Dao2 z2 = m.createDao(Dao2.class);
        assertTrue(z2.isDao2());
    }

    //////

    @Test
    public void filters1() throws Exception {
        DaoInvoker m = app.create(app.getConf().getConf("dao/invoker/filters1"), DefaultDaoInvoker.class);
        Dao1 d = m.createDao(Dao1.class);
        d.isDao2();

    }

    @Test
    public void filters1_1() throws Exception {
        DaoInvoker m = app.bean(DaoService.class).getDaoInvoker("filters1");
        Dao1 d = m.createDao(Dao1.class);
        d.isDao2();
    }

    @Test
    public void context_initer() throws Exception {
        DaoInvoker m = app.create(DefaultDaoInvoker.class);
        DaoClassDef cd = m.getDaoClassDef(ContextIniterDao.class);
        Map<String, Object> map1 = new HashMap<>();
        DaoContext ctx = m.invokeDao((x) -> {
            x.getBeanFactory().registerBean("map1", map1);
        }, cd.getMethods().get("m1"));
        //
        assertEquals(map1.get("k1"), "v1");
        Map<String, Object> map2 = (Map<String, Object>) ctx.bean("map2");
        assertEquals(map1.get("k1"), "v1");
        assertEquals(map2.get("k2"), "v2");
    }

    @Test
    public void abstract_impl_bad_1() throws Exception {
        DaoInvoker m = app.create(DefaultDaoInvoker.class);
        try {
            AbstractDao1_bad z = m.createDao(AbstractDao1_bad.class);
            fail();
        } catch (Exception e) {
            System.out.println("В абстрактном классе нужно иметь метод impl");
            utils.showError(e);
        }
    }

    @Test
    public void abstract_impl_ok_2() throws Exception {
        DaoInvoker m = app.create(DefaultDaoInvoker.class);
        AbstractDao1_ok z = m.createDao(AbstractDao1_ok.class);
        System.out.println(z.getClass());
        assertEquals(z.m1("1", 2), "m1 from AbstractDao1_impl: 1,2");
        assertEquals(z.m2(), "m2 from AbstractDao1_ok");
        AbstractDao1_impl.RES = "";
        z.m3(4, 5);
        assertEquals(AbstractDao1_impl.RES, "m3 from AbstractDao1_impl: 4,5");
    }

    @Test
    public void abstr_arg_name() throws Exception {
        DaoInvoker m = app.create(DefaultDaoInvoker.class);
        DaoClassDef cd = m.getDaoClassDef(AbstractDao1_ok.class);
        for (var mt : cd.getMethods()) {
            System.out.println(mt.getName());
            for (var pr : mt.getParams()) {
                System.out.println("  " + pr.getName());
            }
        }
        abstract_impl_ok_2();
    }

    @Test
    public void daoHolderItem_link() throws Exception {
        utils.logOn();
        DaoService svc = app.bean(DaoService.class);
        DaoHolder h = svc.getDaoHolder("test1");
        //
        String a = (String) h.invokeDao("dao11/method1");
        assertEquals(a, "dao11/method1");
    }

}
