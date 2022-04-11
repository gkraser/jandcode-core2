package jandcode.core.dbm.genid;

import jandcode.core.dbm.test.*;
import org.junit.jupiter.api.*;

public class GenId_Test extends Dbm_Test {

    @Test
    public void service1() throws Exception {
        GenIdService gSvc = getModel().bean(GenIdService.class);
        utils.delim("genid");
        for (GenId genId : gSvc.getGenIds()) {
            System.out.println(genId.getName() + ":" + genId.getStart() + "|" +
                    genId.getStep() + "drv: " + genId.getDriver().getName());
        }
        utils.delim("drivers");
        for (GenIdDriver drv : gSvc.getDrivers()) {
            System.out.println(drv.getName());
        }
    }

}
