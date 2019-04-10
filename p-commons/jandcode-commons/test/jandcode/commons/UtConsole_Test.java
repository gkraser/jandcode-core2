package jandcode.commons;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class UtConsole_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
/*
        System.setProperty(UtConsole.SYSPROPERTY_CONSOLECHARSET,"cp866");
        UtConsole.setupConsoleCharset();
*/
        System.out.println("Привет");
    }


}
