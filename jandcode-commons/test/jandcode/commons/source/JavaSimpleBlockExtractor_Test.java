package jandcode.commons.source;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class JavaSimpleBlockExtractor_Test extends Utils_Test {

    String getBlock(String src) throws Exception {
        JavaSimpleBlockExtractor ex = new JavaSimpleBlockExtractor();
        ex.load().fromString(src);
        return ex.getResult();
    }

    @Test
    public void test1() throws Exception {
        String s;

        //
        s = getBlock("{}");
        assertEquals(s, "");
        //
        s = getBlock("{def a='';if ()a{b='ssss',s='''ss}s'''}}");
        assertEquals(s, "def a='';if ()a{b='ssss',s='''ss}s'''}");
        //
        s = getBlock("{def a='';if ()a{/*b=}}*/'ssss',s='''ss}s'''}}");
        assertEquals(s, "def a='';if ()a{/*b=}}*/'ssss',s='''ss}s'''}");
        //
        s = getBlock("{def a='';if ()a{//b=}}\n*/'ssss',s='''ss}s'''}}");
        assertEquals(s, "def a='';if ()a{//b=}}\n" +
                "*/'ssss',s='''ss}s'''}");
    }


}
