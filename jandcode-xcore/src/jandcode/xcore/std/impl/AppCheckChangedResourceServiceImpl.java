package jandcode.xcore.std.impl;

import jandcode.xcore.*;
import jandcode.xcore.impl.*;
import jandcode.xcore.std.*;
import org.slf4j.*;

public class AppCheckChangedResourceServiceImpl extends BaseComp implements AppCheckChangedResourceService {

    protected static Logger log = LoggerFactory.getLogger(App.class);

    public void checkChangedResource(CheckChangedResourceInfo info) throws Exception {
        if (!(getApp() instanceof AppImpl)) {
            return;
        }
        AppImpl a = (AppImpl) getApp();
        String ff = a.findModifyRtSource();
        if (ff == null) {
            return;
        }
        log.info("found changed rt: " + ff);
        info.needRestartApp();
    }

}
