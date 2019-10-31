package jandcode.core.jsa;


import jandcode.commons.*;
import jandcode.core.jsa.utils.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class CssUrlRebase_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        String s = UtFile.loadString(utils.getTestFile("data/css/url1.css"));
        CssUrlRebase z = new CssUrlRebase();
        String s1 = z.rebase(s, "a/b/c/d", "d/s/a", "/jc/");
        System.out.println(s1);

    }

    @Test
    public void test2() throws Exception {
        String s = UtFile.loadString(utils.getTestFile("data/css/url2.css"));
        CssUrlRebase z = new CssUrlRebase();
        String s1 = z.rebase(s, "a/b/c/d", "d/s/a", "/jc/");
        System.out.println(s1);

    }


}
