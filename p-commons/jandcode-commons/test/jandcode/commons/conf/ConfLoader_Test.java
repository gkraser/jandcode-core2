package jandcode.commons.conf;

import jandcode.commons.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void set1() throws Exception {
        Conf x = UtConf.create();
        ConfLoader ldr = UtConf.createLoader(x);
        //
        String f = utils.getTestFile("data/set1.cfx");
        ldr.load().fromFile(f);
        //
        conf.printConf(x);
        assertEquals(x.getString("a/v1"), "11");
        assertEquals(x.getString("a/v2"), "");
    }


}
