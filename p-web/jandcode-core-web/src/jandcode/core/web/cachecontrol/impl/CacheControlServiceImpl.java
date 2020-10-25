package jandcode.core.web.cachecontrol.impl;

import jandcode.core.*;
import jandcode.core.web.cachecontrol.*;

import java.util.*;

public class CacheControlServiceImpl extends BaseComp implements CacheControlService {

    protected CacheControlHolder cacheControlHolder;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        cacheControlHolder = getApp().create(CacheControlHolder.class);
        cacheControlHolder.add(getApp().getConf().getConfs("web/cache-control"));
    }

    public List<CacheControl> getCacheControls() {
        return cacheControlHolder.getItems();
    }

    public CacheControl findCacheControl(String path) {
        return cacheControlHolder.findForPath(path);
    }
}
