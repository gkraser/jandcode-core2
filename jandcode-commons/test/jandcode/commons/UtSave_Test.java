package jandcode.commons;

import jandcode.commons.io.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.io.*;

public class UtSave_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        UtSave.toFile(new StringSaver("z-привет"), new File("temp/1"));
        UtSave.toFile(new StringSaver("z-привет"), new File("temp/2"), "windows-1251");
    }


}
