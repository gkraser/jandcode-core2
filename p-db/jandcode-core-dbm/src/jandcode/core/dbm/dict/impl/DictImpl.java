package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.DictData;
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
    private DictHandler handler;

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

        // handler
        s = this.conf.getString("handler");
        if (UtString.empty(s)) {
            throw new XError("handler атрибут не установлен");
        }
        this.handler = (DictHandler) getModel().create(s);

    }

    //////

    public Conf getConf() {
        return conf;
    }

    public Domain getDomain() {
        return domain;
    }

    public DictHandler getHandler() {
        return handler;
    }

    public String getDefaultField() {
        return defaultField.getName();
    }

    //////

    public Store createStore() {
        return getModel().bean(ModelStoreService.class).createStore(getDomain());
    }

    public DictData createDictData() {
        return new DictDataImpl(this, createStore());
    }

    public DictData resolveIds(Collection ids) throws Exception {
        DictDao dao = getModel().bean(ModelDaoService.class).getDaoInvoker().createDao(DictDao.class);
        return dao.resolveIds(this, ids);
    }

}
