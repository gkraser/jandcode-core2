package jandcode.core.jsa;

import jandcode.core.jsa.utils.*;
import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SvgIconGenerator_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        SvgIconGenerator g = app.create(SvgIconGenerator.class);
        g.add("jandcode/core/jsa/data/icons/**/*.svg");
        String s = g.generate();
        System.out.println(s);
        assertTrue(s.indexOf("'caret-down':") != -1);
        System.out.println(g.getUsingFiles());
    }


}
