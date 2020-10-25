package jandcode.core.web.virtfile.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.web.virtfile.*;

import java.util.*;

/**
 * Хранилище для правил Cache-Control
 */
public class FileCacheControlHolder extends BaseComp {

    private NamedList<FileCacheControl> items = new DefaultNamedList<>("file-cache-control [{0}] not found");

    public void add(Collection<Conf> lst) {
        for (Conf x : lst) {
            add(x);
        }
    }

    protected void add(Conf conf) {
        FileCacheControl ft = getApp().create(conf, FileCacheControlImpl.class);
        items.add(0, ft);
    }

    public FileCacheControl find(String name) {
        return items.find(name);
    }

    public List<FileCacheControl> getItems() {
        return items;
    }

    public FileCacheControl findForPath(String path) {
        path = UtVDir.normalize(path);
        for (FileCacheControl it : items) {
            if (UtVDir.matchPath(it.getMask(), path)) {
                return it;
            }
        }
        return null;
    }

}
