package jandcode.jc.std;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class JdkInfo_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        utils.outTable(System.getProperties());
    }


    @Test
    public void test_version() throws Exception {
        String a = new JdkInfo().getJdkVersion();
        System.out.println("[" + a + "]");
    }

    @Test
    public void test_ideaLangLevel() throws Exception {
        JdkInfo z = new JdkInfo();
        assertEquals(z.getIdeaLanguageLevelForJdkVersion("8"), "JDK_1_8");
        assertEquals(z.getIdeaLanguageLevelForJdkVersion("9"), "JDK_1_9");
        assertEquals(z.getIdeaLanguageLevelForJdkVersion("14"), "JDK_14");
    }


}
