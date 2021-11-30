package jandcode.commons.conf;

import jandcode.commons.*;
import jandcode.commons.conf.impl.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConfXmlSaver_Test extends CustomConf_Test {

    @Test
    public void test1() throws Exception {
        Conf x = Conf.create();
        x.setValue("a", 1);
        x.setValue("b", 2);
        x.setValue("#0", 3);
        x.setValue("плохой", 4);
        x.setValue("i", 5);
        x.setValue("x-prop", 6);

        Conf x1 = Conf.create();
        x1.setValue("c", 3);

        x.setValue("bb", x1);
        x.setValue("#1", x1);
        x.setValue("плохой1", x1);
        x.setValue("x-prop1", x1);

        ConfXmlSaver sv = new ConfXmlSaver(x);
        String s = sv.save().toString();

        //System.out.println(s);
        assertEquals(s, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>\n" +
                "    <a>1</a>\n" +
                "    <b>2</b>\n" +
                "    <i>3</i>\n" +
                "    <prop x-name=\"плохой\">4</prop>\n" +
                "    <prop x-name=\"i\">5</prop>\n" +
                "    <prop x-name=\"x-prop\">6</prop>\n" +
                "    <bb>\n" +
                "        <c>3</c>\n" +
                "    </bb>\n" +
                "    <i>\n" +
                "        <c>3</c>\n" +
                "    </i>\n" +
                "    <prop x-name=\"плохой1\">\n" +
                "        <c>3</c>\n" +
                "    </prop>\n" +
                "    <prop x-name=\"x-prop1\">\n" +
                "        <c>3</c>\n" +
                "    </prop>\n" +
                "</root>");
    }


}
