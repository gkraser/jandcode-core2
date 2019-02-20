package jandcode.commons.conf.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.io.*;

import java.io.*;

/**
 * Реализация загрузки одного файла в conf из формата json
 */
public class ConfFileLoader_json implements ILoader {

    private final Conf root;
    private final ConfLoaderContext loader;

    public ConfFileLoader_json(Conf root, ConfLoaderContext loader) {
        this.root = root;
        this.loader = loader;
    }

    public void loadFrom(Reader reader) throws Exception {
        StringLoader ldr = new StringLoader();
        ldr.loadFrom(reader);
        Object ob = UtJson.fromJson(ldr.getResult());
        Conf tmp = UtConf.create(ob);
        for (String key : tmp.keySet()) {
            loader.addOrigin(tmp, key, 0);
        }
        this.root.join(tmp);
    }

    public LoadFrom load() {
        return new LoadFrom(this);
    }
}
