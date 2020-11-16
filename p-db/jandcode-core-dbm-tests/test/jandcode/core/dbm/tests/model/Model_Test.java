package jandcode.core.dbm.tests.model;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class Model_Test extends App_Test {

    ModelService svc;

    public void setUp() throws Exception {
        super.setUp();

        svc = app.bean(ModelService.class);
    }

    @Test
    public void test1() throws Exception {
        for (ModelDef md : svc.getModels()) {
            utils.delim(md.getName());
            List<String> lst = new ArrayList<>();
            md.getIncludedModels().forEach(a -> {
                lst.add(a.getName());
            });
            System.out.println(lst);
            utils.delim(md.getName() + " conf in model");
            System.out.println(UtConf.save(md.getInst().getConf()).toString());
            utils.delim(md.getName() + " conf original");
            System.out.println(UtConf.save(md.getConf()).toString());
        }
    }

    @Test
    public void test_name() throws Exception {
        Model m;
        m = svc.getModel("test.2");
        assertEquals(m.getName(), "test.2");
        m = svc.getModel("test.2.inst");
        assertEquals(m.getName(), "test.2.inst");
    }

    @Test
    public void test_instance() throws Exception {
        //
        Model m = svc.getModel("test.2.inst");
        assertEquals(m.getName(), "test.2.inst");
        assertEquals(m.getModelDef().isInstance(), true);
        assertEquals(m.getModelDef().getInstanceOf().getName(), "test.2");
        //
        m = svc.getModel("test.2");
        assertEquals(m.getName(), "test.2");
        assertEquals(m.getModelDef().isInstance(), false);
        assertSame(m.getModelDef().getInstanceOf(), m.getModelDef());

    }

    @Test
    public void test_instance_dbsource() throws Exception {
        //
        Model m = svc.getModel("test.2.inst");
        assertEquals(m.getDbType(), "mysql");
        assertEquals(m.getDbSource().getProps().get("flag.db.mysql"), "1");
    }

    @Test
    public void test_no_dbdriver() throws Exception {
        Model m = svc.getModel("test.2");
        DbSource dbs = m.getDbSource();
        assertEquals(dbs.getDbDriver().getName(), "base");
    }


    @Test
    public void test_module() throws Exception {
        Model m = svc.getModel("test.2");
        assertEquals(m.getModelDef().getModule().getName(), AppConsts.MODULE_APP);
    }


}
