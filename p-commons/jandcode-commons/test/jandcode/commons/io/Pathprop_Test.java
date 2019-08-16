package jandcode.commons.io;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class Pathprop_Test extends Utils_Test {

    @Test
    public void test_getPathprop() throws Exception {
        Pathprop p = new Pathprop();
        System.out.println(p.getPathprop(UtilsConsts.PATHPROP_COREROOT, utils.getTestPath()));
    }

    @Test
    public void test_getPathprops() throws Exception {
        Pathprop p = new Pathprop();
        System.out.println(p.getPathprops(utils.getTestPath()));
    }

}
