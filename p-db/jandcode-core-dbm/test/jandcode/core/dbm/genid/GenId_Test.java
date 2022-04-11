package jandcode.core.dbm.genid;

import jandcode.core.dbm.test.*;
import org.junit.jupiter.api.*;

public class GenId_Test extends Dbm_Test {

    @Test
    public void service1() throws Exception {
        GenIdService gSvc = getModel().bean(GenIdService.class);
    }

}
