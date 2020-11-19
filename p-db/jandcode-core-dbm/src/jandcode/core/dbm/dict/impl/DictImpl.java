package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.domain.*;

public class DictImpl extends BaseModelMember implements Dict {

    public static final String DEFAULT_DOMAIN = "dict.base";
    public static final String DEFAULT_FIELD = "text";

    private Conf conf;
    private Field defaultField;
    private Domain domain;
    private Class daoClass;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        this.conf = cfg.getConf();

        //
        String s;

        // domain
        s = this.conf.getString("domain", DEFAULT_DOMAIN);
        this.domain = getModel().bean(DomainService.class).getDomain(s);

        // defaultField
        s = this.conf.getString("defaultField", DEFAULT_FIELD);
        this.defaultField = this.domain.f(s);

        // dao
        s = this.conf.getString("dao");
        if (UtString.empty(s)) {
            throw new XError("dao атрибут не установлен: {0}", this.conf.origin());
        }
        this.daoClass = UtClass.getClass(s);

    }

    //////

    public Conf getConf() {
        return conf;
    }

    public Domain getDomain() {
        return domain;
    }

    public Class getDaoClass() {
        return daoClass;
    }

    public String getDefaultField() {
        return defaultField.getName();
    }

}
