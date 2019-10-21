package jandcode.core.apex.ajc;

import jandcode.commons.*;
import jandcode.jc.*;
import jandcode.jc.impl.*;

public class ApexJcMain extends MainProduct {

    public void run(String[] args, String mainInclude) {
        setMainInclude(mainInclude);
        String appdir = System.getProperty(JcConsts.PROP_APP_DIR);
        if (UtString.empty(appdir)) {
            appdir = UtFile.getWorkdir();
        }
        if (!run(args, appdir, null, false)) {
            System.exit(1);
        }
    }

}
