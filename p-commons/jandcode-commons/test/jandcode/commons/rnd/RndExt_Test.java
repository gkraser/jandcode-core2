package jandcode.commons.rnd;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class RndExt_Test extends Utils_Test {

    public static class RndMy extends RndExt {

    }

    @Test
    public void test1() throws Exception {
        RndMy r = new RndMy();
        for (int i = 0; i < 20; i++) {
            System.out.println(r.text(Rnd.ERN_CHARS, 40, 100, 20));
        }
    }


}
