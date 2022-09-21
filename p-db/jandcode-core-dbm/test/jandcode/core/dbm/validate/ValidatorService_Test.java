package jandcode.core.dbm.validate;

import jandcode.core.dbm.test.*;
import jandcode.core.dbm.validate.data.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorService_Test extends Dbm_Test {

    @Test
    public void service1() throws Exception {
        ValidatorService svc = getModel().bean(ValidatorService.class);
        System.out.println(svc);
        //
        ValidatorDef vd = svc.getValidators().get("v1");
        Validator1 v = (Validator1) vd.createInst();
        //
        assertEquals(v.getA1(), "1");
        assertSame(v.getModel(), getModel());
    }


}
