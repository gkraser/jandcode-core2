package jandcode.core.dao;

import jandcode.core.dao.data.*;
import jandcode.core.dao.impl.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DaoManager_Test extends App_Test {

    @Test
    public void test1() throws Exception {

        DaoManager m = app.create(DaoManagerImpl.class);
        DaoClassDef c = new DaoClassDefImpl(Dao2.class);

        Object res = m.invokeMethod(c.getMethods().get("sum2"), 2, 4);
        System.out.println("res2=" + res);

        res = m.invokeMethod(c.getMethods().get("sum"), 2, 4);
        System.out.println("res=" + res);

    }

    @Test
    public void test2() throws Exception {

        DaoManager m = app.create(DaoManagerImpl.class);

        Dao2 z = m.createDao(Dao2.class);

        int res = z.sum2(2, 4);
        System.out.println("res2=" + res);

        res = z.sum(2, 4);
        System.out.println("res=" + res);

        try {
            z.getContext();
            fail();
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    public void groovy1() throws Exception {
        DaoManager m = app.create(DaoManagerImpl.class);
        DaoGroovy1 z = m.createDao(DaoGroovy1.class);
        z.dummy1();
    }

    @Test
    public void groovy2() throws Exception {
        DaoManager m = app.create(DaoManagerImpl.class);
        ModuleInfoDao z = m.createDao(ModuleInfoDao.class);
        z.getModules();
    }


}
