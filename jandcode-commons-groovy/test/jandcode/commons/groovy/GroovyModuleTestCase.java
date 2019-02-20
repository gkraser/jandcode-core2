package jandcode.commons.groovy;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.test.*;

public abstract class GroovyModuleTestCase extends Utils_Test {

    private static GroovyCompiler compiler;

    public String loadText(String fn) throws Exception {
        StringLoader ldr = new StringLoader();
        UtLoad.fromRes(ldr, fn);
        return ldr.getResult();
    }

    public GroovyCompiler getCompiler() {
        if (compiler == null) {
            compiler = UtGroovy.createCompiler();
        }
        return compiler;
    }

}
