package jandcode.core.dbm.tests.domain;

import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.domain.db.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DomainDb_Test extends App_Test {

    DomainService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(ModelService.class).getModel("domain.1.inst").bean(DomainService.class);
    }

    @Test
    public void test_external() throws Exception {
        Assertions.assertEquals(svc.domain("ext.tab.3").bean(DomainDb.class).isExternal(), false);
        assertEquals(svc.domain("ext.tab.2").bean(DomainDb.class).isExternal(), true);
        assertEquals(svc.domain("ext.tab.1").bean(DomainDb.class).isExternal(), true);
    }

    @Test
    public void test_index() throws Exception {
        Domain d = svc.domain("db_tab2");
        DomainDb dd = d.bean(DomainDb.class);

        assertEquals(dd.getIndexes().size(), 2);
        DomainDbIndex idx;

        idx = dd.getIndexes().get(0);
        assertEquals(idx.getName(), "i1");
        assertEquals(idx.getFields().size(), 1);
        assertEquals(idx.getFields().get(0).getName(), "f_db_tab1");
        assertEquals(idx.getFields().get(0).isDesc(), false);
        assertEquals(idx.isUnique(), true);

        idx = dd.getIndexes().get(1);
        assertEquals(idx.getName(), "i2");
        assertEquals(idx.getFields().size(), 2);
        assertEquals(idx.getFields().get(1).getName(), "f1");
        assertEquals(idx.getFields().get(1).isDesc(), true);
        assertEquals(idx.isUnique(), false);

    }


}
