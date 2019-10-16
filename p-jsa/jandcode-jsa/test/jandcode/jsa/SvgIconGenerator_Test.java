package jandcode.jsa;

import jandcode.core.web.test.*;
import jandcode.jsa.utils.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SvgIconGenerator_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        SvgIconGenerator g = app.create(SvgIconGenerator.class);
        g.add("jandcode/jsa/data/icons/**/*.svg");
        String s = g.generate();
        System.out.println(s);
        assertTrue(s.indexOf("'caret-down':") != -1);
    }


}
