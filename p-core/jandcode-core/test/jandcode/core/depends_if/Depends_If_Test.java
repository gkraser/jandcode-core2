package jandcode.core.depends_if;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class Depends_If_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        System.out.println(app.getEnv().isDev());
    }


}
