package jandcode.core.web.cachecontrol.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.web.cachecontrol.*;

import java.util.*;

/**
 * Хранилище для правил Cache-Control
 */
public class CacheControlHolder extends BaseComp {

    private NamedList<CacheControl> items = new DefaultNamedList<>("cache-control [{0}] not found");

    public void add(Collection<Conf> lst) {
        for (Conf x : lst) {
            add(x);
        }
    }

    protected void add(Conf conf) {
        CacheControl ft = getApp().create(conf, CacheControlImpl.class);
        items.add(0, ft);
    }

    public CacheControl find(String name) {
        return items.find(name);
    }

    public List<CacheControl> getItems() {
        return items;
    }

    public CacheControl findForPath(String path) {
        path = UtVDir.normalize(path);
        for (CacheControl it : items) {
            if (UtVDir.matchPath(it.getMask(), path)) {
                return it;
            }
        }
        return null;
    }

}
