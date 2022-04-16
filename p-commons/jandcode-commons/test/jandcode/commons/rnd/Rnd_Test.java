package jandcode.commons.rnd;

import jandcode.commons.datetime.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class Rnd_Test extends Utils_Test {

    Rnd r;

    public void setUp() throws Exception {
        super.setUp();
        r = Rnd.create(545);
    }

    //////

    @Test
    public void test_num() throws Exception {
        for (int i = 1; i <= 100; i++) {
            String s = String.format("%3d", r.num(10, 20));
            System.out.print(s);
            if (i % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    @Test
    public void test_num_1_1() throws Exception {
        for (int i = 1; i <= 100; i++) {
            String s = String.format("%3d", r.num(0, 1));
            System.out.print(s);
            if (i % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    @Test
    public void test_bool() throws Exception {
        for (int i = 1; i <= 100; i++) {
            String s = String.format("%6b", r.bool());
            System.out.print(s);
            if (i % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    @Test
    public void test_bool_tf() throws Exception {
        int t = 0;
        int f = 0;
        for (int i = 1; i <= 100; i++) {
            boolean v = r.bool(30, 20);
            if (v) {
                t++;
            } else {
                f++;
            }
            String s = String.format("%6b", v);
            System.out.print(s);
            if (i % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("t=" + t + " f=" + f);
    }

    @Test
    public void test_choiceChar() throws Exception {
        for (int i = 1; i <= 100; i++) {
            String s = String.format("%2c", r.choice("qaz"));
            System.out.print(s);
            if (i % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    @Test
    public void test_choiceFromArr() throws Exception {
        String[] arr = new String[]{"i1", "i2", "i3"};
        for (int i = 1; i <= 100; i++) {
            String s = String.format("%3s", r.choice(arr));
            System.out.print(s);
            if (i % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    @Test
    public void test_text() throws Exception {
        for (int i = 0; i < 20; i++) {
            System.out.println(r.text(Rnd.ERN_CHARS, 40, 100, 20));
        }
    }

    @Test
    public void test_doub() throws Exception {
        for (int i = 1; i <= 100; i++) {
            String s = String.format("%9.5f ", r.doub(-10.9, 11.3, 3));
            System.out.print(s);
            if (i % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    @Test
    public void test_date() throws Exception {
        XDate min = XDate.create(1999, 12, 1);
        XDate max = XDate.create(1999, 12, 5);
        for (int i = 1; i <= 100; i++) {
            String s = r.date(min, max).toString() + "  ";
            System.out.print(s);
            if (i % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    @Test
    public void test_datetime() throws Exception {
        XDateTime min = XDateTime.create(1999, 12, 1);
        XDateTime max = XDateTime.create(1999, 12, 5);
        for (int i = 1; i <= 100; i++) {
            String s = r.datetime(min, max).toString() + "  ";
            System.out.print(s);
            if (i % 5 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

}