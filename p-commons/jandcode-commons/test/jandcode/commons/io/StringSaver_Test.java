package jandcode.commons.io;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringSaver_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        String s = "hello,saver";
        StringSaver h = new StringSaver(s);
        String s1 = UtSave.toString(h, "utf-8");
        assertEquals(s1, s);
    }


}
