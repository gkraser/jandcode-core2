package jandcode.commons;

import jandcode.commons.impl.*;
import jandcode.commons.str.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SubstVarParser_Test extends Utils_Test {

    class Exp1 implements ISubstVar {
        public String onSubstVar(String v) {
            return "[" + v.toUpperCase() + "]";
            //return "[" + v + "]";
        }
    }

    private void check(String src, String dest) throws Exception {
        Exp1 e = new Exp1();
        SubstVarParser p = new SubstVarParser('$', '{', '}', e);
        UtLoad.fromString(p, src);
        assertEquals(dest, p.getResult());
    }

    @Test
    public void test0() throws Exception {
        check("${q}d", "[Q]d");
    }

    @Test
    public void test1() throws Exception {
        check(null, "");
        check("", "");
        check("1", "1");
        check("12", "12");
        check("123", "123");

        check("sx${q}dc", "sx[Q]dc");
        check("s${q}d", "s[Q]d");
        check("s${q}", "s[Q]");
        check("${q}d", "[Q]d");

        check("${q}", "[Q]");
        check("${q1}${q2}", "[Q1][Q2]");
        check("a${q1}${q2}", "a[Q1][Q2]");
        check("${q1}${q2}a", "[Q1][Q2]a");
        check("1${q}", "1[Q]");
        check("${q}2", "[Q]2");
        check("1${q}2", "1[Q]2");
        check("1${q1}2${q2}", "1[Q1]2[Q2]");
        check("${q1${q2}}", "[Q1${Q2]}");

        check("\\\\${q}", "\\[Q]");
        check("a\\\\${q}", "a\\[Q]");
        check("ab\\\\${q}", "ab\\[Q]");
        check("ab\\\\${q}ab\\\\${q}", "ab\\[Q]ab\\[Q]");

        check("\\${q}", "${q}");
        check("\\${q}\\${a}", "${q}${a}");
        check("x\\${q}c\\${a}v", "x${q}c${a}v");
        check("xv\\${q}cb\\${a}vn", "xv${q}cb${a}vn");
    }

    @Test
    public void testSpeedFromRealReader() throws Exception {
        String s = UtString.repeat("--${azseede}--", 50);
        System.out.println(s.length());
        int n = 1000;

        stopwatch.start();
        for (int i = 0; i < n; i++) {
            Exp1 e = new Exp1();
            SubstVarParser p = new SubstVarParser('$', '{', '}', e);
            UtLoad.fromString(p, s);
        }
        stopwatch.stop((long) n);
    }

    @Test
    public void testSpeedFromString() throws Exception {
        String s = UtString.repeat("--${azseede}--", 50);
        System.out.println(s.length());
        int n = 10000;

        stopwatch.start();
        for (int i = 0; i < n; i++) {
            Exp1 e = new Exp1();
            SubstVarParser p = new SubstVarParser('$', '{', '}', e);
            p.loadFrom(s);
        }
        stopwatch.stop((long) n);
    }


}
