package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

public class DomainServiceImpl extends BaseModelMember implements DomainService {

    private NamedList<Domain> domains;
    private NamedList<Domain> tmpDomains = new DefaultNamedList<>();
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

    public Domain createDomain(Conf x) {
        Domain dd = getModel().create(x, DomainImpl.class, inst -> {
            ((DomainImpl) inst).domainService = this;
        });
        return dd;
    }

    public Field createField(Conf x, Domain forDomain) {
        return forDomain.create(x, FieldImpl.class);
    }

    public DomainBuilder createDomainBuilder(String parentDomain) {
        return new DomainBuilderImpl(this, parentDomain);
    }

    public Field findField(String name) {
        if (UtString.empty(name)) {
            return null;
        }

        int a = name.indexOf('/');
        if (a == -1) {
            // просто имя
            Domain d = findDomain(DomainConsts.DOMAIN_SYSTEM_FIELDS);
            if (d == null) {
                return null;
            }
            return d.findField(name);
        }

        String n1 = name.substring(0, a);
        String n2 = name.substring(a + 1);
        if ("ref".equals(n2)) {
            // имя Abonent/ref
            Domain d = findDomain(n1);
            if (d == null) {
                return null;
            }
            // есть такой домен Abonent
            // используем временный динамический домен с одним полем
            String tmpdName = name.replace('/', '.');
            Domain tmpd = tmpDomains.find(tmpdName);
            if (tmpd == null) {
                synchronized (this) {
                    tmpd = tmpDomains.find(tmpdName);
                    if (tmpd == null) {
                        DomainBuilder builder = createDomainBuilder(DbmConsts.BASE);
                        builder.addField(n1, "domain/" + n1 + "/ref/default");
                        tmpd = builder.createDomain(tmpdName);
                        tmpDomains.add(tmpd);
                    }
                }
            }
            return tmpd.findField(n1);
        }

        // имя Abonent/name
        Domain d = findDomain(n1);
        if (d == null) {
            return null;
        }
        return d.findField(n2);
    }

    //////

}
