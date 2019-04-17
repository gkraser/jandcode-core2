package jandcode.dao;

import jandcode.core.test.*;
import jandcode.dao.data.*;
import jandcode.dao.impl.*;
import org.junit.jupiter.api.*;

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

        z.getContext();
    }


}