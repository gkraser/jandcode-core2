package jandcode.core.dbm.domain.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.store.*;

public class DomainServiceImpl extends BaseModelMember implements DomainService {

    private NamedList<Domain> domains;
    private DomainConfHolder domainConfHolder;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        // ленивая конфигурация...
    }

    protected DomainConfHolder getDomainConfHolder() {
        if (domainConfHolder == null) {
            synchronized (this) {
                if (domainConfHolder == null) {
                    domainConfHolder = createDomainConfHolder();
                }
            }
        }
        return domainConfHolder;
    }

    protected DomainConfHolder createDomainConfHolder() {
        DomainConfHolder h = new DomainConfHolder();
        h.buildConf(getModel().getConf());
        return h;
    }

    public NamedList<Domain> getDomains() {
        if (domains == null) {
            synchronized (this) {
                if (domains == null) {
                    domains = createDomains();
                }
            }
        }
        return domains;
    }

    protected NamedList<Domain> createDomains() {
        NamedList<Domain> res = new DefaultNamedList<>();

        //
        for (Conf x : getDomainConfHolder().getDomainConf().getConfs()) {
            Domain dd = getModel().create(x, DomainImpl.class, inst -> {
                ((DomainImpl) inst).domainService = this;
            });
            res.add(dd);
        }

        return res;
    }

    //////

    public Domain createDomain(Conf x, String name) {
        Conf domainConf = getDomainConfHolder().expandDomainConf(x, name);
        return getModel().create(domainConf, DomainImpl.class, inst -> {
            ((DomainImpl) inst).domainService = this;
        });
    }

    public DomainBuilder createDomainBuilder(String parentDomain) {
        return new DomainBuilderImpl(this, parentDomain);
    }

    public Store createStore(Domain domain) {
        Store store = getApp().bean(StoreService.class).createStore();
        //
        for (Field f : domain.getFields()) {
            StoreField sf = store.addField(f.getName(), f.getStoreDataType());
            if (f.hasDict()) {
                sf.setDict(f.getDict());
            }
        }
        //
        return store;
    }

}
