package jandcode.commons.error;

import jandcode.commons.error.*;
import jandcode.commons.error.impl.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class ErrorFormatter_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        try {
            throw new Exception("a");
        } catch (Exception e) {
            ErrorFormatter z = new ErrorFormatterDefault(true, true, false);
            System.out.println(z.getMessage(e));
        }
    }

    @Test
    public void test2() throws Exception {
        try {
            throw new Exception("a");
        } catch (Exception e) {
            ErrorFormatter z = new ErrorFormatterDefault(false, false, false);
            System.out.println(z.getMessage(e));
        }
    }

}
