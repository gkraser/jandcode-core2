package jandcode.core.dbm.verdb;

import jandcode.core.dbm.test.*;
import org.junit.jupiter.api.*;

public class VerdbModule_Test extends Dbm_Test {

    @Test
    public void test1() throws Exception {
        VerdbService svc = getModel().bean(VerdbService.class);
        System.out.println(svc.getVerdbModules().size());
        System.out.println(svc.getVerdbModules().get(0).getModuleName());
        System.out.println(svc.getVerdbModules().get(0).getName());
    }


}
