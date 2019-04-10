package jandcode.jc.impl.log;

import jandcode.commons.*;
import jandcode.commons.simxml.*;

public class LogConfigurator {

    public static void configure(boolean verbose) throws Exception {
        SimXml x = new SimXmlNode();
        x.load().fromFileObject("res:jandcode/jc/impl/log/logback-config-template.xml");

        if (verbose) {
            x.setValue("logger@name=jc.console:level", "debug");
        }

        //
        UtLog.logOn(x.save().toString());
    }

}
