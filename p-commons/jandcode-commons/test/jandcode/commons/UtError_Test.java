package jandcode.commons;

import jandcode.commons.error.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class UtError_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        try {
            try {
                try {
                    throw new Exception("e1");
                } catch (Exception e) {
                    throw new Exception("e2", e);
                }
            } catch (Exception e) {
                throw new Exception(e);
            }
        } catch (Exception e) {
            ErrorInfo ei = UtError.createErrorInfo(e);
            utils.delim("text");
            System.out.println(ei.getText());
            utils.delim("stack filtered");
            System.out.println(ei.getTextStack(true));
            utils.delim("stack all");
            System.out.println(ei.getTextStack(false));
        }
    }


}
