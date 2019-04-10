package jandcode.web;

import jandcode.core.test.*;
import jandcode.web.type.*;
import org.junit.jupiter.api.*;

public class Types_Test extends App_Test {

    private void findType(Class cls) {
        WebService svc = app.bean(WebService.class);
        //
        TypeDef a = svc.findType(cls);
        String s = "for: " + cls.getName() + " = ";
        if (a == null) {
            s += "NULL";
        } else {
            s += a.getName();
        }
        System.out.println(s);
    }

    @Test
    public void test1() throws Exception {
        WebService svc = app.bean(WebService.class);
        for (TypeDef d : svc.getTypes()) {
            System.out.println(d.getName() + "=" + d.getClass() + "=" + d.getCls());
        }

        //
        findType(Type1.class);
        findType(Type2.class);
        findType(Type3.class);
        findType(this.getClass());
    }

}
