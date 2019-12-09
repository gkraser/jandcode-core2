package jandcode.core.dao

import jandcode.core.dao.data.*
import jandcode.core.dao.impl.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

public class DaoManager_Groovy_Test extends App_Test {

    @Test
    public void test1() throws Exception {

        DaoManager m = app.create(DaoManagerImpl.class);
        DaoClassDef c = new DaoClassDefImpl(Dao2.class);

        Object res = m.invokeDao(c.getMethods().get("sum2"), 2, 4);
        System.out.println("res2=" + res);

        res = m.invokeDao(c.getMethods().get("sum"), 2, 4);
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
            System.out.println("Только dao методы можно вызывать вне процесса исполнения dao");
            System.out.println("context не существут вне вызова dao");
            utils.showError(e);
        }
    }

    @Test
    public void groovy1() throws Exception {
        DaoManager m = app.create(DaoManagerImpl.class);
        DaoGroovy1 z = m.createDao(DaoGroovy1.class);
        z.dummy1();
    }


}
