package jandcode.core.dbm.dao;

import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.data.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Dao_Test extends App_Test {

    Model model;
    Model model2;
    ModelDaoService daoSvc;

    public void setUp() throws Exception {
        super.setUp();
        //

        model = app.bean(ModelService.class).getModel("default");
        model2 = app.bean(ModelService.class).getModel("model2");
        daoSvc = model.bean(ModelDaoService.class);
    }

    @Test
    public void check_model_link() throws Exception {
        //
        CheckModelLink dao = daoSvc.createDao(CheckModelLink.class);
        assertEquals(dao.m1(), "m1-ok-default");
        //
        dao = model2.bean(ModelDaoService.class).createDao(CheckModelLink.class);
        assertEquals(dao.m1(), "m1-ok-model2");
    }

    @Test
    public void db_link() throws Exception {
        utils.logOn();
        //
        stopwatch.start();
        DbmDbTestSvc z = testSvc(DbmDbTestSvc.class);
        //
        ModelDaoService svc = z.getModel().bean(ModelDaoService.class);
        DbLink dao = svc.createDao(DbLink.class);
        String s = dao.m1();
        assertEquals(s, "m1-ok-test1-111");
        stopwatch.stop();
    }


}
