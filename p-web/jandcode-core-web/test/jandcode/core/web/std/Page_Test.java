package jandcode.core.web.std;

import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

public class Page_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        String s = web.renderGsp("data/page1.gsp");
        System.out.println(s);

    }


}
