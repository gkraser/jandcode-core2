package jandcode.core.dbm.dao;

import jandcode.commons.*;
import jandcode.core.dao.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.data.*;
import jandcode.core.dbm.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Dao_Test extends Dbm_Test {

    Model model;
    Model model2;
    ModelDaoService daoSvc;
    DaoInvoker daoInvoker;

    public void setUp() throws Exception {
        super.setUp();
        //

        model = app.bean(ModelService.class).getModel("default");
        model2 = app.bean(ModelService.class).getModel("model2");
        daoSvc = model.bean(ModelDaoService.class);
        daoInvoker = daoSvc.getDaoInvoker();
    }

    @Test
    public void check_model_link() throws Exception {
        //
        CheckModelLink dao = daoInvoker.createDao(CheckModelLink.class);
        assertEquals(dao.m1(), "m1-ok-default");
        //
        dao = model2.bean(ModelDaoService.class).getDaoInvoker().createDao(CheckModelLink.class);
        assertEquals(dao.m1(), "m1-ok-model2");
    }

    @Test
    public void db_link() throws Exception {
        utils.logOn();
        //
        stopwatch.start();
        //
        ModelDaoService svc = getModel().bean(ModelDaoService.class);
        DbLink dao = svc.getDaoInvoker().createDao(DbLink.class);
        String s = dao.m1();
        assertEquals(s, "m1-ok-testdb-111");
        stopwatch.stop();
    }

    @Test
    public void mdb_link() throws Exception {
        MdbLink dao = getMdb().createDao(MdbLink.class);
        String s = dao.m1();
        assertEquals(s, "m1-ok-mdb-testdb-111");
    }

    @Test
    public void invoker_resolver() throws Exception {
        DaoHolder h1 = app.bean(DaoService.class).getDaoHolder("h1");
        for (var z : h1.getItems()) {
            System.out.println(UtString.padRight(z.getName(), 40) + " " + h1.resolveDaoInvokerName(z));
        }
        String s;
        s = (String) h1.invokeDao("p1/checkModelLink/m1");
        assertEquals(s, "m1-ok-testdb");
        s = (String) h1.invokeDao("p1/dbLink/m1");
        assertEquals(s, "m1-ok-testdb-111");
    }


}
