package jandcode.wax.web;

import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

public class WaxThemeService_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        WaxThemeService svc = app.bean(WaxThemeService.class);
        System.out.println(svc.getThemeNames());
        System.out.println(svc.getThemeFile("std"));
        System.out.println(svc.getThemeFile("stdXXX"));
        System.out.println(svc.getThemeFile(null));
    }

}
