package jandcode.commons.io;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class Pathprop_Test extends Utils_Test {

    @Test
    public void test_getPathprops() throws Exception {
        Pathprop p = new Pathprop();
        System.out.println(p.getPathprops(utils.getTestPath()));
    }

}
