package jandcode.commons.conf;

import jandcode.commons.*;
import jandcode.commons.conf.impl.*;
import org.junit.jupiter.api.*;

public class ConfFileLoader_cfx_Test extends CustomConf_Test {

    @Test
    public void test1() throws Exception {
        //language=XML
        String s = "<root>\n" +
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
        ConfFileLoader_cfx ldr = new ConfFileLoader_cfx(x, (ConfLoaderImpl) UtConf.createLoader(x));
        ldr.load().fromString(s);

        conf.printConf(x);
        Assertions.assertEquals(conf.toMap(x).toString(), "" +
                "{a=2, dbm={}, domains={domain1={qaz={tag1=1}, #1={tag1=2}}, " +
                "comment=Hello}, fields={field={}, #1={a=1}, #2={a=2}}}");
    }

}
