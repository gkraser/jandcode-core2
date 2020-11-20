package jandcode.core.dbm.mdb.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

public class MdbDomainImpl implements IMdbDomain {

    private Mdb mdb;
    private DomainService domainService;

    public MdbDomainImpl(Mdb mdb) {
        this.mdb = mdb;
    }

    private DomainService getDomainService() {
        if (domainService == null) {
            domainService = mdb.getModel().bean(DomainService.class);
        }
        return domainService;
    }

    //////

    public NamedList<Domain> getDomains() {
        return getDomainService().getDomains();
    }

    public Domain getDomain(String name) {
        return getDomainService().getDomain(name);
    }

    public Domain findDomain(String name) {
        return getDomainService().findDomain(name);
    }

    public DomainBuilder createDomainBuilder(String parentDomain) {
        return getDomainService().createDomainBuilder(parentDomain);
    }

    public Domain createDomain(Conf x, String name) {
        return getDomainService().createDomain(x, name);
    }

    public Store createStore(Domain domain) {
        return getDomainService().createStore(domain);
    }

}
