package jandcode.commons.conf;

import jandcode.commons.*;
import org.junit.jupiter.api.*;

public class ConfOrigin_Test extends CustomConf_Test {

    @Test
    public void origin_cfx1() throws Exception {
        Conf x = UtConf.create();
        ConfLoader ldr = UtConf.createLoader(x);
        //
        String f = utils.getTestFile("data/file1.cfx");
        ldr.load().fromFile(f);
        //
        conf.printConf(conf.toConfWithOrigin(x));
    }

    @Test
    public void origin1() throws Exception {
        Conf x = UtConf.create();
        //
        String f = utils.getTestFile("data/origin1-named.cfx");
        UtConf.load(x).fromFile(f);
        //
        conf.printConf(conf.toConfWithOrigin(x));
    }

    @Test
    public void clone1() throws Exception {
        Conf x = UtConf.create();
        //
        String f = utils.getTestFile("data/origin1-named.cfx");
        UtConf.load(x).fromFile(f);
        //
        Conf x1 = x.cloneConf();
        Conf x2 = x.getConf("field/a1").cloneConf();
        //
        conf.printConf(conf.toConfWithOrigin(x));
        conf.printConf(conf.toConfWithOrigin(x1));
        conf.printConf(conf.toConfWithOrigin(x2));
    }

    @Test
    public void join1() throws Exception {
        Conf x = UtConf.create();
        //
        String f = utils.getTestFile("data/origin1-named.cfx");
        UtConf.load(x).fromFile(f);
        //
        Conf x1 = UtConf.create();
        x1.join(x);
        //
        conf.printConf(conf.toConfWithOrigin(x1));
    }


}
