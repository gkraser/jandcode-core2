package jandcode.commons.groovy;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class Md5Speed extends Utils_Test {

    @Test
    public void test1() throws Exception {

        String s = UtString.repeat("~123456789", 500);

        int n = 10000;

        stopwatch.start();

        for (int i = 0; i < n; i++) {
            String s1 = UtString.md5Str(s);
        }

        stopwatch.stop((long) n);
    }


}
