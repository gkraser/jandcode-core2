package jandcode.commons.conf;

import jandcode.commons.*;
import org.junit.jupiter.api.*;

public class ConfLoader_Test extends CustomConf_Test {

    @Test
    public void test1() throws Exception {
        Conf x = UtConf.create();
        ConfLoader ldr = UtConf.createLoader(x);
        //
        String f = utils.getTestFile("data/file1.cfx");
        ldr.load().fromFile(f);
        //
        conf.printConf(x);
        utils.delim("xml");
        System.out.println(UtConf.save(x).toString());
    }

    @Test
    public void test_json1() throws Exception {
        Conf x = UtConf.create();
        ConfLoader ldr = UtConf.createLoader(x);
        //
        String f = utils.getTestFile("data/file3.json");
        ldr.load().fromFile(f);
        //
        conf.printConf(x);
    }

}
