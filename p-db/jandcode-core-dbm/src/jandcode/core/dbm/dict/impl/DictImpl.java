package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.store.*;

public class DictImpl extends BaseModelMember implements Dict {

    public static final String DEFAULT_DOMAIN = "dict.base";
    public static final String DEFAULT_FIELD = "text";

    private Conf conf;
    private Field defaultField;
    private Domain domain;
    private DictHandler handler;
    private boolean loadable;
    private boolean multiValue;

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

        // multiValue
        this.multiValue = this.conf.getBoolean("multiValue", true);

        // handler
        s = this.conf.getString("handler");
        if (UtString.empty(s)) {
            throw new XError("handler атрибут не установлен");
        }
        this.handler = (DictHandler) getModel().create(s);
        this.loadable = handler instanceof IDictHandlerLoadDict;
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

    public boolean isLoadable() {
        return loadable;
    }

    //////

    public Store createStore() {
        return getModel().bean(DomainService.class).createStore(getDomain());
    }

    public DictData createDictData() {
        return new DictDataImpl(this, createStore());
    }

    //////

    public boolean isMultiValue() {
        return multiValue;
    }

}
