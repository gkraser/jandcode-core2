package jandcode.commons.attrparser;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AttrParser_Test extends Utils_Test {

    void parse(String s, String res) {
        AttrParser p = new AttrParser();
        p.loadFrom(s);
        String exp = p.getResult().toString();
        System.out.println(exp);
        assertEquals(exp, res);
    }

    @Test
    public void test0() throws Exception {
        //parse("=a", "[:a]")
    }

    @Test
    public void test1() throws Exception {
        parse("", "{}");
        parse("attr1=value1 a2-1='value 1' ", "{attr1=value1, a2-1=value 1}");
        parse(" a1 b2 c3 ", "{a1=, b2=, c3=}");
        parse("^a1 ^* b2", "{^a1=, ^*=, b2=}");
        parse("a1", "{a1=}");
        parse("=", "{}");
        parse("==", "{}");
        parse("a=", "{a=}");
        parse("=a", "{a=}");
        parse("a", "{a=}");
        parse("a=1 b=2", "{a=1, b=2}");
        parse("a=\"\" b=2", "{a=, b=2}");
    }


}
