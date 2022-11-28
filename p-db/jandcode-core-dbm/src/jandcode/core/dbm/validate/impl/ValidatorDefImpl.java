package jandcode.core.dbm.validate.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.validate.*;

public class ValidatorDefImpl extends BaseModelMember implements ValidatorDef {

    private Conf conf;
    private Class cls;

    public ValidatorDefImpl(String name, Conf conf, Model model) {
        this.conf = conf;
        setName(name);
        setModel(model);
        this.cls = UtClass.getClass(conf.getString("class"));
    }

    public Conf getConf() {
        return conf;
    }

    public Validator createInst() {
        return (Validator) getModel().create(this.cls);
    }

    public Class getCls() {
        return cls;
    }

}
