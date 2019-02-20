package jandcode.jc.impl.lib;

import jandcode.commons.*;
import jandcode.jc.*;

/**
 * Библиотека jdk-tools
 */
public class LibJdkTools extends LibCustom {

    public LibJdkTools(Ctx ctx) {
        super(ctx);
        name = "jdk-tools";
    }

    public String getVersion() {
        return System.getProperty("java.version");
    }

    public String getJar() {
        String home = System.getProperty("java.home");
        return UtFile.abs(UtFile.join(home, "../lib/tools.jar"));
    }

    public boolean isSys() {
        return true;
    }

}
