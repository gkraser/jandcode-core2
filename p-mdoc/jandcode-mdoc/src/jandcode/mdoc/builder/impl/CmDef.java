package jandcode.mdoc.builder.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Регистрация для команды
 */
public class CmDef extends BaseComp {

    private Class cls;

    public CmDef(App app, Conf conf) {
        setApp(app);
        setName(conf.getName());
        this.cls = UtClass.getClass(conf.getString("class"));
    }

    public Class getCls() {
        return this.cls;
    }

}
