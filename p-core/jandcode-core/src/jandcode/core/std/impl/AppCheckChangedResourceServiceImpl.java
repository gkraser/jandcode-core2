package jandcode.core.std.impl;

import jandcode.core.*;
import jandcode.core.impl.*;
import jandcode.core.std.*;
import org.slf4j.*;

public class AppCheckChangedResourceServiceImpl extends BaseComp implements AppCheckChangedResourceService {

    protected static Logger log = LoggerFactory.getLogger(App.class);

    public void checkChangedResource(CheckChangedResourceInfo info) throws Exception {
        if (!(getApp() instanceof AppImpl)) {
            return;
        }
        AppImpl a = (AppImpl) getApp();
        String ff = a.findModifyConfSource();
        if (ff == null) {
            return;
        }
        log.info("found changed conf: " + ff);
        info.needRestartApp();
    }

}
