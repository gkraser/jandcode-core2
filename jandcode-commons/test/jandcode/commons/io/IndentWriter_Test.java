package jandcode.commons.io;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class IndentWriter_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        StringWriter f = new StringWriter();
        IndentWriter ff = new IndentWriter(f);

        ff.write("hello\n\n\n1\n");
        ff.write("hello2");
        ff.write("hello3\n");
        ff.write("hello4\n");
        ff.indentInc();
        ff.write("indent+\n");
        ff.write("indent+\n");
        ff.indentDec();
        ff.write("indent-\n");
        ff.write("indent-\n");
        ff.indentInc();
        ff.write("indent+\n");
        ff.indentInc();
        ff.write("indent+\n");
        ff.indentInc();
        ff.write("indent+\n");
        ff.indentInc("*|*|*");
        ff.write("indent+\n\n");
        ff.write("indent+\n\n");
        ff.write("indent+\n\n");
        ff.indentDec(5);
        ff.write("indent+\n\n");

        String s = f.toString().trim();
        assertEquals(s, "hello\n" +
                "\n" +
                "\n" +
                "1\n" +
                "hello2hello3\n" +
                "hello4\n" +
                "    indent+\n" +
                "    indent+\n" +
                "indent-\n" +
                "indent-\n" +
                "    indent+\n" +
                "        indent+\n" +
                "            indent+\n" +
                "            *|*|*indent+\n" +
                "            *|*|*\n" +
                "            *|*|*indent+\n" +
                "            *|*|*\n" +
                "            *|*|*indent+\n" +
                "            *|*|*\n" +
                "            indent+");

    }


}
