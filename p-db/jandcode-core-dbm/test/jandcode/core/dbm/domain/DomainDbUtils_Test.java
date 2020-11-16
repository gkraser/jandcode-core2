package jandcode.core.dbm.domain;

import jandcode.commons.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.db.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DomainDbUtils_Test extends App_Test {

    ModelService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(ModelService.class);

    }

    @Test
    public void test_getMaxLengthIdnInDb() throws Exception {
        Model model = svc.getModel("domain.1");
        //
        System.out.println(UtConf.save(model.getConf()).toString());
        //
        DomainDbUtils ut = new DomainDbUtils(model);
        assertEquals(ut.getIdnMaxLength(), 63);

        model = svc.getModel("domain.1.inst.oracle");
        ut = new DomainDbUtils(model);
        assertEquals(ut.getIdnMaxLength(), 30);
    }

    @Test
    public void test_makeShortIdn() throws Exception {
        Model model = svc.getModel("domain.1");
        DomainDbUtils ut = new DomainDbUtils(model);

        String s = "012345678901234567890123456789";
        System.out.println(s.length());

        String s1 = ut.makeShortIdn(s, 20);
        assertEquals(s1.length(), 20);
        assertEquals(s1, "01234567890_0D60638F");
    }


}
