package jandcode.commons.source;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class JcDirectiveExtractor_Test extends Utils_Test {

    protected void ch(JcDirective d, String name, String[] prms) {
        assertEquals(d.hasName(name), true);
        assertEquals(d.getParams().size(), prms.length);
        for (int i = 0; i < d.getParams().size(); i++) {
            assertEquals(d.getParams().get(i), prms[i]);
        }
    }

    @Test
    public void test_1() throws Exception {
        String s = "aa\n//#jc s1  \n//#jc s2 none\n//#jc s3 true false,true\nbbb//#jc s4 ";
        JcDirectiveExtractor e = new JcDirectiveExtractor(s);

        ch(e.getItems().get(0), "s1", new String[]{});
        ch(e.getItems().get(1), "s2", new String[]{"none"});
        ch(e.getItems().get(2), "s3", new String[]{"true false", "true"});
        ch(e.getItems().get(3), "s4", new String[]{});
    }


}
