package jandcode.commons.io;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class XmlSaver_Test extends Utils_Test {

    class Saver1 extends XmlSaver {
        protected void onSaveXml() throws Exception {
            startNode("root");
            writeAttr("a1", "v1");
            startNode("n1");
            writeAttr("a2", "v2");
            writeCommentNode("comment");
            writeText("text1");
            stopNode();
            stopNode();
        }
    }

    @Test
    public void test1() throws Exception {
        String s = UtSave.toString(new Saver1());
        assertEquals(s, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root a1=\"v1\">\n" +
                "    <n1 a2=\"v2\">\n" +
                "        <!--comment-->text1\n" +
                "    </n1>\n" +
                "</root>");
    }


}
