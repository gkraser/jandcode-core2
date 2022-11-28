package jandcode.core.dbm.validate;

import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DomainValidatorHolder_Test extends Dbm_Test {

    private void outValidators(List<ValidatorDef> lst) {
        for (ValidatorDef vd : lst) {
            System.out.println(vd.getName());
            System.out.println(vd.getConf().toString());
            System.out.println("--");
        }
    }

    @Test
    public void empty1() throws Exception {
        Domain d = getMdb().getDomain("empty1");
        DomainValidatorHolder dv = d.bean(DomainValidatorHolder.class);
        assertEquals(dv.getValidators().size(), 0);
    }

    @Test
    public void attr1() throws Exception {
        Domain d = getMdb().getDomain("attr1");
        DomainValidatorHolder dv = d.bean(DomainValidatorHolder.class);
        outValidators(dv.getValidators());
        assertEquals(dv.getValidators().size(), 3);
    }

    @Test
    public void child1() throws Exception {
        Domain d = getMdb().getDomain("child1");
        DomainValidatorHolder dv = d.bean(DomainValidatorHolder.class);
        outValidators(dv.getValidators());
        assertEquals(dv.getValidators().size(), 9);
    }


}
