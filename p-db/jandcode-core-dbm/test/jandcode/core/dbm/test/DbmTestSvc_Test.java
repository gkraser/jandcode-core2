package jandcode.core.dbm.test;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbmTestSvc_Test extends App_Test {

    DbmTestSvc dbm = testSvc(DbmTestSvc.class);

    @Test
    public void test1() throws Exception {
        String s = dbm.getModel().getName();
        assertEquals(s, "test1");
    }

}
