package jandcode.jsa.jsmodule;

import jandcode.jsa.jsmodule.impl.*;
import jandcode.web.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RequireExtractor_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        String s;
        List<String> r;

        RequireExtractor re = new RequireExtractor();

        s = "hello require('aaa  '); require('xxx'+'yyy'); require(\"bbb\"); hello";
        r = re.extractRequire(s);
        assertEquals(r.toString(), "[aaa, bbb]");

        s = "hello require(\n'./../aaa/b  '); require('xxx'+'yyy'); require(\"bbb/c\\d\"); hello";
        r = re.extractRequire(s);
        assertEquals(r.toString(), "[./../aaa/b, bbb/c\\d]");

        s = "hello require('aaa/*.js'); require('./*.js'); hello";
        r = re.extractRequire(s);
        assertEquals(r.toString(), "[aaa/*.js, ./*.js]");

        s = "hello require('aaa$1/*/*.js'); require('./**/*.js'); \n require('./**/*.js');hello";
        r = re.extractRequire(s);
        assertEquals(r.toString(), "[aaa$1/*/*.js, ./**/*.js, ./**/*.js]");
    }


    @Test
    public void test_badFiles_jquery() throws Exception {
        String s;
        List<String> r;

        RequireExtractor re = new RequireExtractor();
        s = web.getWebService().getFile("jandcode/jsa/data/requires/jquery.js.txt").loadText();
        stopwatch.start("jquery.js.txt");
        r = re.extractRequire(s);
        stopwatch.stop("jquery.js.txt");
        assertEquals(r.toString(), "[]");
    }

    //!!! ОЧЕНЬ ДОЛГО РАБОТАЕТ!!!
    //@Test
    public void test_badFiles_jqueryMin() throws Exception {
        String s;
        List<String> r;

        RequireExtractor re = new RequireExtractor();
        s = web.getWebService().getFile("jandcode/jsa/data/requires/jquery.min.js.txt").loadText();
        stopwatch.start("jquery.min.js.txt");
        r = re.extractRequire(s);
        stopwatch.stop("jquery.min.js.txt");
        assertEquals(r.toString(), "[]");
    }

}
