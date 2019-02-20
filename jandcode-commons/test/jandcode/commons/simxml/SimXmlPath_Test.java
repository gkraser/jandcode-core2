package jandcode.commons.simxml;

import jandcode.commons.simxml.impl.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class SimXmlPath_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {

        SimXmlPath p = new SimXmlPath("node/b@id=q1:b1");
        Assertions.assertEquals(p.hasAttrName(), true);
        Assertions.assertEquals(p.getAttrName(), "b1");

        Assertions.assertEquals(p.getItems().size(), 2);

        SimXmlPath.Item it = p.getItems().get(0);
        Assertions.assertEquals(it.getNodeName(), "node");
        Assertions.assertEquals(it.hasAttrName(), false);
        Assertions.assertEquals(it.getAttrName(), null);
        Assertions.assertEquals(it.getAttrValue(), null);

        it = p.getItems().get(1);
        Assertions.assertEquals(it.getNodeName(), "b");
        Assertions.assertEquals(it.hasAttrName(), true);
        Assertions.assertEquals(it.getAttrName(), "id");
        Assertions.assertEquals(it.getAttrValue(), "q1");

        //
    }


    @Test
    public void test2() throws Exception {

        SimXmlPath p = new SimXmlPath("option@name=MAIN_CLASS_NAME:value");
        Assertions.assertEquals(p.hasAttrName(), true);
        Assertions.assertEquals(p.getAttrName(), "value");

        Assertions.assertEquals(p.getItems().size(), 1);

        SimXmlPath.Item it = p.getItems().get(0);
        Assertions.assertEquals(it.getNodeName(), "option");
        Assertions.assertEquals(it.hasAttrName(), true);
        Assertions.assertEquals(it.getAttrName(), "name");
        Assertions.assertEquals(it.getAttrValue(), "MAIN_CLASS_NAME");

        //
    }


}
