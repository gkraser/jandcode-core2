package jandcode.commons.conf;

import jandcode.commons.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConfLoader_If_Test extends CustomConf_Test {

    Conf load(String data) throws Exception {
        Conf x = UtConf.create();
        ConfLoader ldr = UtConf.createLoader(x);
        //language=XML
        ldr.load().fromString(data);
        return x;
    }

    @Test
    public void if1() throws Exception {
        Conf x = load(
                //language=XML
                "<root>\n" +
                        "    <x-if/>\n" +
                        "</root>"
        );
        //
        //conf.printConf(x);
        assertEquals(x.size(), 0);
    }

    @Test
    public void if2() throws Exception {
        Conf x = load(
                //language=XML
                "<root>\n" +
                        "    <x-if>\n" +
                        "        <a/>\n" +
                        "    " +
                        "</x-if>\n" +
                        "</root>"
        );
        //
        //conf.printConf(x);
        assertEquals(x.size(), 0);
    }

    @Test
    public void if3() throws Exception {
        Conf x = load(
                //language=XML
                "<root>\n" +
                        "    <x-set v1=\"val\"/>\n" +
                        "    <x-if v1=\"val\">\n" +
                        "        <a v1=\"#{v1}\"/>\n" +
                        "    " +
                        "</x-if>\n" +
                        "</root>"
        );
        //
        //conf.printConf(x);
        assertEquals(x.size(), 1);
        assertEquals(x.getString("a/v1"), "val");
    }

    @Test
    public void if4() throws Exception {
        Conf x = load(
                //language=XML
                "<root>\n" +
                        "    <x-set v1=\"valX\"/>\n" +
                        "    <x-if-not v1=\"val\">\n" +
                        "        <a v1=\"#{v1}\"/>\n" +
                        "    " +
                        "</x-if-not>\n" +
                        "</root>"
        );
        //
        //conf.printConf(x);
        assertEquals(x.size(), 1);
        assertEquals(x.getString("a/v1"), "valX");
    }


}
