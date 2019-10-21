package jandcode.core.apex.ajc;

import jandcode.jc.impl.*;

public class ApexJc extends MainProduct {

    public void run(String[] args, String mainInclude) {
        setMainInclude(mainInclude);
        if (!run(args, null, null, false)) {
            System.exit(1);
        }
    }

}
