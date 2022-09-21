package jandcode.core.dbm.validate.impl;

import jandcode.commons.conf.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.validate.*;

public class ValidatorDefImpl extends BaseModelMember implements ValidatorDef {

    private Conf conf;
    private String fieldName;

    public ValidatorDefImpl(String name, Conf conf, Model model) {
        this.conf = conf;
        setName(name);
        setModel(model);
        this.fieldName = conf.getString("field");
    }

    public Conf getConf() {
        return conf;
    }

    public Validator createInst() {
        return (Validator) getModel().create(this.conf);
    }

    public String getFieldName() {
        return fieldName;
    }

}
