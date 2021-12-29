package jandcode.core.dbm.domain;

import jandcode.commons.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DomainBuilder_Test extends Dbm_Test {

    DomainService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(ModelService.class).getModel("domain.1").bean(DomainService.class);
    }

    @Test
    public void test0() throws Exception {
        DomainBuilder b = svc.createDomainBuilder("base");
        b.addField("test0", "string");
        Domain d = b.createDomain("name1");
        //
        System.out.println(UtConf.save(d.getConf()).toString());
        //
        assertEquals(d.getName(), "name1");
        assertEquals(d.getFields().size(), 1);
        assertEquals(d.f("test0").getDbDataType().getName(), "string");
        assertFalse(d.getConf().containsKey("parent"));
    }

    @Test
    public void no_name() throws Exception {
        DomainBuilder b = svc.createDomainBuilder("base");
        Domain d = b.createDomain(null);
        //
        System.out.println(UtConf.save(d.getConf()).toString());
        //
        assertEquals(d.getName(), "noname");
    }

    @Test
    public void test1() throws Exception {
        DomainBuilder b = svc.createDomainBuilder("tab1.3");
        b.addField("test0", "string");
        b.addField("test1", "domain/tab1.1/field/f1");
        b.addField("test2", "tab1.1/f1");
        Domain d = b.createDomain("name1");
        System.out.println(UtConf.save(d.getConf()).toString());
    }

    @Test
    public void test2() throws Exception {
        DomainBuilder b = svc.createDomainBuilder("tab1.3");
        b.addField("test0", "string");
        b.addField("test1", "tab1.3/f1");
        b.addField("test4", "tab1.3/f4");
        b.addField("test5", "tab1.3/ref_t1");
        b.addField("test6", "tab1.3/ref");
        Domain d = b.createDomain("name1");
        System.out.println(UtConf.save(d.getConf()).toString());
    }


}
