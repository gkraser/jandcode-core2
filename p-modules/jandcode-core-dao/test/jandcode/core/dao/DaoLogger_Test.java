package jandcode.core.dao;

import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class DaoLogger_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        utils.logOn();
        DaoService svc = app.bean(DaoService.class);
        DaoHolder h = svc.getDaoHolder("test1");
        //
        Object a = h.invokeDao("dao1/sum", 5, 6);
    }


}
