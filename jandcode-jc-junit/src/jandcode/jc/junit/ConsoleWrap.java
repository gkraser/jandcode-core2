package jandcode.jc.junit;

import java.io.*;

/**
 * Заменяет стандартную консоль враппером и делает враппер системной консолью.
 */
public class ConsoleWrap {

    public static PrintStream saveOut;
    public static PrintStream saveErr;

    public static PrintStream dummyOut;
    public static PrintStream dummyErr;

    public static void wrap() {
        if (saveOut != null) {
            return;
        }
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        dummyOut = new PrintStream(ba);
        dummyErr = new PrintStream(bb);

        saveOut = System.out;
        System.setOut(dummyOut);

        saveErr = System.err;
        System.setErr(dummyErr);
    }

    /**
     * Печать на реальной консоле
     */
    public static void println(String s) {
        if (saveOut != null) {
            saveOut.println(s);
        } else {
            System.out.println(s);
        }
    }

    /**
     * Печать на реальной консоле
     */
    public static void print(String s) {
        if (saveOut != null) {
            saveOut.print(s);
        } else {
            System.out.print(s);
        }
    }

    /**
     * flush на реальной консоле
     */
    public static void flush() {
        if (saveOut != null) {
            saveOut.flush();
        } else {
            System.out.flush();
        }
    }

}
