package jandcode.core.web.webxml;

import jandcode.commons.simxml.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class WebXml_Test extends Utils_Test {

    @Test
    public void test_saveToXml() throws Exception {
        DefaultWebXmlFactory f = new DefaultWebXmlFactory();
        WebXml w = f.createWebXml();
        WebXmlUtils u = new WebXmlUtils();
        SimXml x = u.saveToXml(w);
        System.out.println(x.save().toString());
    }

}
