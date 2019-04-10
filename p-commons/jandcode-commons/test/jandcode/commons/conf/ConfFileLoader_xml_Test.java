package jandcode.commons.conf;

import jandcode.commons.*;
import jandcode.commons.conf.impl.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConfFileLoader_xml_Test extends CustomConf_Test {

    @Test
    public void test1() throws Exception {
        //language=XML
        String s = "<root attr1=\"1\">\n" +
                "    <a>1</a>\n" +
                "    <a>2</a>\n" +
                "    <dbm/>\n" +
                "    <domains>\n" +
                "        <!--@ Hello -->\n" +
                "        <domain1 name=\"qaz\" tag1=\"1\"/>\n" +
                "        <domain1 name=\"i\" tag1=\"2\"/>\n" +
                "    </domains>\n" +
                "    <fields>\n" +
                "        <field/>\n" +
                "        <i a=\"1\"/>\n" +
                "        <b a=\"2\" x-name=\"i\"/>\n" +
                "    </fields>\n" +
                "</root>";
        Conf x = UtConf.create();
        ConfFileLoader_xml ldr = new ConfFileLoader_xml(x, (ConfLoaderImpl) UtConf.createLoader(x));
        ldr.load().fromString(s);

        conf.printConf(x);
        assertEquals(conf.toMap(x).toString(), "" +
                "{attr1=1, #1={$name=a, text=1}, #2={$name=a, text=2}, #3={$name=dbm}, " +
                "#4={$name=domains, comment=Hello, #2={$name=domain1, name=qaz, tag1=1}, " +
                "#3={$name=domain1, name=i, tag1=2}}, #5={$name=fields, #1={$name=field}," +
                " #2={$name=i, a=1}, #3={$name=b, a=2, x-name=i}}}");
    }

    @Test
    public void include1() throws Exception {
        Conf x = UtConf.create();
        UtConf.load(x).fromFile(utils.getTestFile("data/xml-inc1.xml"));
        conf.printConf(x);
    }

    @Test
    public void include2() throws Exception {
        Conf x = UtConf.create();
        UtConf.load(x).fromFile(utils.getTestFile("data/xml-inc2.xml"));
        conf.printConf(x);
        //
        for (Conf x1: x.getConfs()){
            System.out.println(""+x1.getName()+"=>"+x1.origin());
            System.out.println("----");
            for (Conf x2: x1.getConfs()){
                System.out.println("     "+x2.getName()+"=>"+x2.origin());
            }
        }
    }

}
