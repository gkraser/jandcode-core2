package test._inc.app;

import jandcode.core.std.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class MyApp1_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        // app - ссылка на приложение: jandcode.core.App
        DataDirService svc = app.bean(DataDirService.class);
        String p = svc.getPath("mypath");
        //...
    }

}
