package jandcode.commons.simxml;

import jandcode.commons.*;
import jandcode.commons.simxml.impl.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SimXml_Test extends Utils_Test {

    @Test
    public void saver1() throws Exception {

        SimXml x = new SimXmlImpl();
        x.setName("node1");
        x.setValue("a1", "v1");
        SimXml x2 = x.addChild("n2");
        x2.setValue("a2", "v2");
        x2.setText("qaz");
        x2.addChild(SimXmlConsts.NODE_COMMENT).setText("comm2");
        x.setValue(":aa1", "aa11");
        x.setValue("n3:aa2", "aa12");
        x.addChild(SimXmlConsts.NODE_COMMENT).setText("comm");
        String s = new SimXmlSaver(x).save().toString();
        //System.out.println(s);
        assertEquals(s, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<node1 a1=\"v1\" aa1=\"aa11\">\n" +
                "    <n2 a2=\"v2\">qaz\n" +
                "        <!--comm2-->\n" +
                "    </n2>\n" +
                "    <n3 aa2=\"aa12\"/>\n" +
                "    <!--comm-->\n" +
                "</node1>");
    }

    @Test
    public void saver_comment() throws Exception {

        SimXml x = new SimXmlImpl();
        SimXml x2 = x.addChild("n2");
        x2.addChild(SimXmlConsts.NODE_COMMENT).setText("comm2");
        x2.addChild(SimXmlConsts.NODE_COMMENT).setText("comm2-1\ncomm2-2");
        SimXml x3 = x2.addChild("n3");
        x2.addChild(SimXmlConsts.NODE_TEXT).setText("text2");
        x2.addChild("n3");
        x2.addChild("n3");
        x2.addChild(SimXmlConsts.NODE_COMMENT).setText("comm3");
        x2.addChild(SimXmlConsts.NODE_TEXT).setText("text2-1\ntext2-2");

        String s = new SimXmlSaver(x).save().toString();
        //System.out.println(s);
        assertEquals(s, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>\n" +
                "    <n2>\n" +
                "        <!--comm2-->\n" +
                "        <!--comm2-1\n" +
                "comm2-2-->\n" +
                "        <n3/>\n" +
                "        text2\n" +
                "        <n3/>\n" +
                "        <n3/>\n" +
                "        <!--comm3-->\n" +
                "        text2-1\n" +
                "text2-2\n" +
                "    </n2>\n" +
                "</root>");
    }


    @Test
    public void test_noname1() throws Exception {
        SimXml x = new SimXmlNode();
        x.load().fromFile(utils.getTestFile("data/textnode1.xml"));
        SimXml node = x.findChild("node");
        assertEquals(node.getChilds().size(), 2);
        assertEquals(node.getChilds().get(0).getName(), "b");
        assertEquals(node.getChilds().get(1).getName(), "#text");
        String s = x.save().toString();
        //System.out.println(s);
        assertEquals(s, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>\n" +
                "    <node>text1\n" +
                "        <b>1</b>\n" +
                "        text2\n" +
                "    </node>\n" +
                "</root>");
    }

    @Test
    public void test_asis1() throws Exception {
        SimXml x = new SimXmlNode();

        SimXmlLoader ldr = new SimXmlLoader(x);
        ldr.setTrimSpace(false);
        ldr.setLoadComment(true);
        ldr.load().fromFile(utils.getTestFile("data/asis1.txt"));

        SimXmlSaver svr = new SimXmlSaver(x);
        svr.setUseIndent(false);

        String s = svr.save().toString();

        String orig = UtFile.loadString(utils.getTestFile("data/asis1.txt"));

        assertEquals(s, orig);
    }

    @Test
    public void getValueByPath() throws Exception {
        SimXml x = new SimXmlNode("doc");
        SimXml x2 = x.addChild("a");
        x2.setText("t1");
        x2.addChild("b").setText("t2");
        assertEquals(x.getValue("a:#text"), "t1");
        assertEquals(x.getValue("a/b:#text"), "t2");
    }


    @Test
    public void entity1() throws Exception {
        SimXml x = new SimXmlNode();
        x.load().fromFile(utils.getTestFile("data/entity1.xml"));
        String s = x.save().toString();
        //System.out.println(s);
        assertEquals(x.getString("e:#text"), ">&<\"");
        assertEquals(x.getString("m:#text"), "");
    }

    @Test
    public void attr1() throws Exception {
        SimXml x = new SimXmlNode();
        x.load().fromFile(utils.getTestFile("data/attr1.xml"));
        String s = x.save().toString();
        //System.out.println(s);
        assertEquals(x.getString("node/b@id=q1:b1"), "22");
        assertEquals(x.getString("node:a1"), "1");
        assertEquals(x.getString("node/b:b1"), "2");
    }

    @Test
    public void set_value_by_path1() throws Exception {
        SimXml x = new SimXmlNode();
        x.setValue("option@name=MAIN_CLASS_NAME:value", "1");
        String s = x.save().toString();
        assertEquals(s, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>\n" +
                "    <option name=\"MAIN_CLASS_NAME\" value=\"1\"/>\n" +
                "</root>");
    }

}