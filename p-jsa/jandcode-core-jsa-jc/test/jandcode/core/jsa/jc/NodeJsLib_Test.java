package jandcode.core.jsa.jc;

import jandcode.commons.simxml.*;
import jandcode.commons.test.*;
import jandcode.core.jsa.jc.impl.*;
import org.junit.jupiter.api.*;

public class NodeJsLib_Test extends Utils_Test {

    @Test
    public void load_save_1() throws Exception {
        String f = utils.getTestFile("data/libs1.nodejs.xml");

        SimXml x1 = new SimXmlNode();
        x1.load().fromFile(f);

        NodeJsLibXmlLoader ldr = new NodeJsLibXmlLoader();
        NodeJsLibList libs = ldr.load(x1);

        SimXml x2 = new SimXmlNode();

        NodeJsLibXmlSaver svr = new NodeJsLibXmlSaver();
        svr.save(libs, x2);

        System.out.println(x2.save().toString());
    }


}
