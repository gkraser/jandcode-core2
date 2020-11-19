package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.store.*;
import jandcode.core.store.*;

import java.util.*;

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
            throw new XError("dao атрибут не установлен");
        }
        this.daoClass = UtClass.getClass(s);
        if (!IResolveDict.class.isAssignableFrom(this.daoClass)) {
            throw new XError("dao-class {0} должен реализовывать интерфейс {1}",
                    this.daoClass.getName(),
                    IResolveDict.class.getName());
        }

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

    //////

    public Store resolveIds(Collection ids) {
        Store store = getModel().bean(ModelStoreService.class).createStore(getDomain());
        IResolveDict dao = (IResolveDict) getModel().bean(ModelDaoService.class).getDaoInvoker().createDao(getDaoClass());
        dao.resolveDict(this, store, ids);
        return store;
    }

}
