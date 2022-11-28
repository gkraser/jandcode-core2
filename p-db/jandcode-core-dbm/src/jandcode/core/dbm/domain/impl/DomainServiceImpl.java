package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
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
        NamedList<Domain> res = new DefaultNamedList<>("Домен не найден: {0}");

        //
        for (Conf x : getDomainConfHolder().getDomainConf().getConfs()) {
            if (UtConf.isTagged(x, "abstract")) {
                continue;
            }
            Domain dd = getModel().create(x, DomainImpl.class, inst -> {
                ((DomainImpl) inst).domainService = this;
            });
            res.add(dd);
            // validate: для полей домена в базе должен быть указан dbdatatype и sqltype
            if (dd.hasTag("db")) {
                for (Field f : dd.getFields()) {
                    if (f.getDbDataType() == null) {
                        throw new XError("Для поля {0} домена {1} не указан dbdatatype", f.getName(), f.getDomain().getName());
                    }
                    if (f.getSqlType() == null) {
                        throw new XError("Для поля {0} домена {1} не указан sqltype", f.getName(), f.getDomain().getName());
                    }
                }
            }
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

    public Domain createDomain(Store store) {
        DomainBuilder b = createDomainBuilder("base");
        for (StoreField f : store.getFields()) {
            Conf fconf = b.addField(f.getName(), f.getStoreDataType().getName());
            if (f.getSize() > 0) {
                fconf.setValue("size", f.getSize());
            }
        }
        return b.createDomain("noname");
    }

    public DomainBuilder createDomainBuilder(String parentDomain) {
        return new DomainBuilderImpl(this, parentDomain);
    }

    public Store createStore(Domain domain) {
        Store store = getApp().bean(StoreService.class).createStore();
        //
        store.setCustomProp(DbmConsts.STORE_PROP_DOMAIN, domain);
        //
        for (Field f : domain.getFields()) {
            StoreField sf = store.addField(f.getName(), f.getStoreDataType().getName());
            if (f.hasDict()) {
                sf.setDict(f.getDict());
            }
            if (f.getScale() != StoreField.NO_SCALE) {
                sf.setScale(f.getScale());
            }
            sf.setTitle(f.getTitle());
        }
        //
        return store;
    }

}
