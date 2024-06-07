package jandcode.commons.conf;

import jandcode.commons.*;
import org.junit.jupiter.api.*;

public class ConfInclude_Test extends CustomConf_Test {

    @Test
    public void test1() throws Exception {
        Conf x = Conf.create();
        UtConf.load(x).fromFile(utils.getTestFile("data/include1.cfx"));
        //
        Conf x1 = UtConf.expandInclude(x, x.getConf("top/second/model2"), "top/second");
        conf.printConf(x1);
    }

}
