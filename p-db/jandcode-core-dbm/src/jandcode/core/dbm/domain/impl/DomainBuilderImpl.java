package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.domain.*;

public class DomainBuilderImpl extends BaseComp implements DomainBuilder {

    private DomainServiceImpl domainSvc;
    private Conf conf;

    public DomainBuilderImpl(DomainServiceImpl domainSvc, String parent) {
        this.domainSvc = domainSvc;
        this.conf = Conf.create("noname");
        if (!UtString.empty(parent)) {
            this.conf.setValue("parent", parent);
        }
    }

    public Conf getConf() {
        return conf;
    }

    public Domain createDomain(String name) {
        return domainSvc.createDomain(this.conf, name);
    }

    public Conf addField(String name, String parent) {
        Conf x = conf.findConf("field/" + name, true);
        if (!UtString.empty(parent)) {
            x.setValue("parent", parent);
        }
        return x;
    }

}
