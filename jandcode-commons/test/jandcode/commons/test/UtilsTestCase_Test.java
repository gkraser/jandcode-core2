package jandcode.commons.test;

import jandcode.commons.*;
import org.junit.jupiter.api.*;

public class UtilsTestCase_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        System.out.println(getTestName());
        System.out.println(utils.getTestPath());
    }

    @Test
    public void test_mem() throws Exception {
        memory.printCur();
        memory.save();
        char a[] = new char[100000];
        a[2] = 1;
        memory.printDiff();

        memory.print(memory.diff());
    }

    @Test
    public void testTimer() throws Exception {
        stopwatch.start();
        StringBuilder sb = new StringBuilder();
        long n = 10000;
        for (int i = 0; i < n; i++) {
            sb.append(UtString.repeat("***", 100));
        }
        stopwatch.stop(n);
    }

    @Test
    public void testTimer2() throws Exception {
        stopwatch.start("timer2");
        StringBuilder sb = new StringBuilder();
        long n = 10000;
        for (int i = 0; i < n; i++) {
            sb.append(UtString.repeat("***", 100));
        }
        stopwatch.stop("timer2");
    }


}
