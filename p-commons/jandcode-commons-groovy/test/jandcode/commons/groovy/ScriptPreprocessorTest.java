package jandcode.commons.groovy;

import jandcode.commons.groovy.impl.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ScriptPreprocessorTest extends GroovyModuleTestCase {

    @Test
    public void test1() throws Exception {
        String s = loadText("/jandcode/commons/groovy/gdata/test1.txt");
        GroovyScriptSplitter p = new GroovyScriptSplitter(s);
        assertEquals(p.getImportPart(), "\r\n" +
                "import jandcode.commons.*\r\n" +
                "import jandcode.commons.groovy.*\r\n" +
                "//#classpath=cp1\r\n" +
                "import jandcode.commons.test.*");
        assertEquals(p.getBodyPart(), "\r\n" +
                "\r\n" +
                "print \"hello\"");
        assertEquals(p.getImportCountLines(), 5);
    }

}
