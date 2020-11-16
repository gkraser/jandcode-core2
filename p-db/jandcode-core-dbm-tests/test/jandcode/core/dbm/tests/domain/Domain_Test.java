package jandcode.core.dbm.tests.domain;

import jandcode.commons.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.tests.domain.data.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Domain_Test extends App_Test {

    DomainService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(ModelService.class).getModel("domain.1").bean(DomainService.class);
    }

    //////

    @Test
    public void test1() throws Exception {
        Assertions.assertEquals(svc.getModel().getName(), "domain.1");
    }

    @Test
    public void no_parent_attr_in_conf() throws Exception {
        Domain d = svc.getDomain("id");
        assertEquals(d.getName(), "id");
        assertFalse(d.getConf().containsKey("parent"));
    }

    @Test
    public void no_bean_attr_in_conf() throws Exception {
        Domain d;
        //
        d = svc.getDomain("base");
        assertFalse(d.getConf().containsKey("bean"));
        //
        d = svc.getDomain("id");
        assertFalse(d.getConf().containsKey("bean"));
    }


    @Test
    public void test_ref1() throws Exception {
        Domain d = svc.getDomain("tab1.3");
        Field f = d.f("ref_t1");
        assertEquals(f.getConf().getString("ref"), "tab1.1");
        //
        Domain d1 = svc.domain("tab1.3");
        assertEquals(d1.f("ref_t1").getConf().getString("ref"), "tab1.1");
    }


    @Test
    public void test_bean1() throws Exception {
        Domain d = svc.getDomain("bean1.1");

        System.out.println(UtConf.save(d.getConf()).toString());

        BeanDomain1 bd;
        BeanField1 bf;

        bd = (BeanDomain1) d.bean("bean-domain-2");
        assertEquals(bd.getAttr1(), "1");
        assertEquals(bd.getAttr2(), "2");

        bd = (BeanDomain1) d.bean("bean-domain-1");
        assertEquals(bd.getAttr1(), "1");

        Field f = d.f("f1");

        bf = (BeanField1) f.bean("bean-field-2");
        assertEquals(bf.getAttr3(), "3");
        assertEquals(bf.getAttr4(), "4");

        bf = (BeanField1) f.bean("bean-field-1");
        assertEquals(bf.getAttr3(), "3");

    }

    @Test
    public void test_include_domain1() throws Exception {
        Domain d = svc.getDomain("include1.1");

        System.out.println(UtConf.save(DomainUtils.saveDomainToRt(d, false)));

    }

    @Test
    public void test_IDomain() throws Exception {
        Domain d = svc.getDomain("tab1.1");

        assertEquals(d.getTitle(), "Tab1-1");
        assertEquals(d.getDbTableName(), "tab1.1");
        assertEquals(d.hasTag("db"), true);

        d = svc.getDomain("tab1.3");
        assertEquals(d.getTitle(), "Tab1-1");
        assertEquals(d.getDbTableName(), "tab1.1");
        assertEquals(d.hasTag("db"), false);

    }

    @Test
    public void test_systemFieldsDomain() throws Exception {
        Domain d = svc.getDomain("system.fields");

        // поля должны быть
        d.f("string");
        d.f("long");

        // поля не должны быть
        assertEquals(d.findField("base"), null);
    }

    @Test
    public void test_notFoundFieldMessage() throws Exception {
        Domain d = svc.getDomain("system.fields");
        try {
            d.f("qazqaz");
            fail();
        } catch (Exception e) {
            utils.showError(e);
        }
    }


}
