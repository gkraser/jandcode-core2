package jandcode.core.dao;

import jandcode.core.dao.data.*;
import jandcode.core.dao.impl.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

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


}
