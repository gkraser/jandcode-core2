package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.conf.*;
import jandcode.core.apx.dbm.sqlfilter.*;
import jandcode.core.dbm.*;

public class SqlFilterWhereDefImpl extends BaseModelMember implements SqlFilterWhereDef {

    private Conf conf;

    public SqlFilterWhereDefImpl(Model model, String name, Conf conf) {
        this.conf = conf;
        setModel(model);
        setName(name);
    }

    public Conf getConf() {
        return conf;
    }

    public SqlFilterWhere createInst() {
        return (SqlFilterWhere) getModel().create(conf);
    }
}
