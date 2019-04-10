package jandcode.jsa.jsmodule;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.regex.*;

public class Regex_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {

        String s = "(?:^\\uFEFF?|[^$_a-zA-Z\\xA0-\\uFFFF.\"'])require\\s*\\(\\s*(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"|'[^'\\\\]*(?:\\\\.[^'\\\\]*)*'|`[^`\\\\]*(?:\\\\.[^`\\\\]*)*`)\\s*\\)";

        String a = "hello require('aaa  '); require('xxx'+'yyy'); require(\"bbb\"); hello";

        Pattern p = Pattern.compile(s, Pattern.DOTALL);
        Matcher m = p.matcher(a);

        while (m.find()) {
            System.out.println(m.groupCount());
            System.out.println("[" + m.group(1) + "]");
        }

    }


    @Test
    public void test_comment() throws Exception {
        String s = "\\/\\*[\\s\\S]*?\\*\\/|([^\\\\:]|^)\\/\\/.*$";

        String a = "hello /*require('aaa  ');\n */ require('xxx'+'yyy//');\n //require(\"bbb\"); hello";

        Pattern p = Pattern.compile(s, Pattern.DOTALL);
        Matcher m = p.matcher(a);
        String s1 = m.replaceAll("");
        System.out.println(s1);

//        while(m.find()) {
//            System.out.println(m.groupCount());
//            System.out.println("[" + m.group(1) + "]");
//        }


    }


}
