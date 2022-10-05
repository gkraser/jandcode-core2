package jandcode.core.dbm.sql.impl;

import jandcode.commons.conf.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

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
