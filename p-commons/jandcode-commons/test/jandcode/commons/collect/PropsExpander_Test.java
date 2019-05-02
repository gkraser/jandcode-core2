package jandcode.commons.collect;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PropsExpander_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        Conf conf = UtConf.create();
        PropsExpander e = new PropsExpander(conf);
        e.setCycleValue("<x>");

        conf.setValue("p2", "2-${p1}");
        conf.setValue("p3", "3-${p2}");
        conf.setValue("p1", "PARAM1");

        conf.setValue("p4", "PARAM4-${p5}");
        conf.setValue("p5", "PARAM5-${p4}");

        conf.setValue("p6", "PARAM6-${p7}");
        conf.setValue("p7", "PARAM7-${p8}");
        conf.setValue("p8", "PARAM8-${p6}");
        
        assertEquals(e.expandAll().toString(), "{p2=2-PARAM1, p3=3-2-PARAM1, p1=PARAM1, " +
                "p4=PARAM4-PARAM5-<x>, p5=PARAM5-PARAM4-<x>, p6=PARAM6-PARAM7-PARAM8-<x>, " +
                "p7=PARAM7-PARAM8-PARAM6-<x>, p8=PARAM8-PARAM6-PARAM7-<x>}");
    }

}
