package jandcode.core.web.type.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.web.type.*;

import java.util.*;

/**
 * type описанный в conf
 */
public class TypeDefConf extends BaseComp implements TypeDef {

    private Class cls;
    private Map<String, String> attrs = new HashMap<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf conf = cfg.getConf();
        //
        for (Map.Entry<String, Object> a : conf.entrySet()) {
            attrs.put(a.getKey(), UtCnv.toString(a.getValue()));
        }
    }

    public Class getCls() {
        if (cls == null) {
            cls = UtClass.getClass(getName());
        }
        return cls;
    }

    public String getAttr(String name) {
        return attrs.get(name);
    }

}
