package jandcode.core.std;

import jandcode.commons.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CfgService_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        CfgService cfg = app.bean(CfgService.class);
        //
        utils.outMap(UtConf.toMap(cfg.getConf()));
        //
        assertEquals(cfg.getConf().getValue("p1"), "url1/call-1");
    }


}
