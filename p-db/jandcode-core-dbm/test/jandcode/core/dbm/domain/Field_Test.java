package jandcode.core.dbm.domain;

import jandcode.commons.variant.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.test.*;
import jandcode.core.store.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Field_Test extends Dbm_Test {

    DomainService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(ModelService.class).getModel("domain.1").bean(DomainService.class);
    }

    //////

    @Test
    public void test_IField() throws Exception {
        Domain d = svc.getDomain("tab1.1");

        assertEquals(d.f("f1").getDataType(), VariantDataType.STRING);
        assertEquals(d.f("f2").getDataType(), VariantDataType.LONG);
        assertEquals(d.f("f2").getTitle(), "F2 title");
        assertEquals(d.f("f2").getTitleShort(), "F2");
        assertEquals(d.f("f2").getDbDataType().getName(), "long");
        //
        assertEquals(d.f("f3").getScale(), 4);
        assertEquals(d.f("f3_1").getScale(), StoreField.NO_SCALE);
        assertEquals(d.f("f3_2").getScale(), StoreField.NO_SCALE);
    }


}
