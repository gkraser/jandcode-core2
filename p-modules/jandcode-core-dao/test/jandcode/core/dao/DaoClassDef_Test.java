package jandcode.core.dao;

import jandcode.core.dao.data.*;
import jandcode.core.dao.impl.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class DaoClassDef_Test extends App_Test {

    void prnMethod(DaoMethodDef m) {
        System.out.println(m.getName() + ": " + m.getMethod());
    }

    void prnClass(DaoClassDef cls) {
        utils.delim(cls.getName());
        System.out.println(cls.getName() + ":");
        for (DaoMethodDef m : cls.getMethods()) {
            prnMethod(m);
        }
        utils.delim();
    }

    @Test
    public void groovy1() throws Exception {
        DaoClassDef cd = new DaoClassDefImpl(DaoGroovy1.class);
        prnClass(cd);
    }


}
