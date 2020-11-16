package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

public class DomainBuilderImpl extends BaseComp implements DomainBuilder {

    private DomainServiceImpl domainSvc;
    private Conf conf;

    public DomainBuilderImpl(DomainServiceImpl domainSvc, String parent) {
        this.domainSvc = domainSvc;
        this.conf = UtConf.create();
        if (!UtString.empty(parent)) {
            this.conf.setValue("parent", parent);
        }
    }

    public Conf getConf() {
        return conf;
    }

    public Domain createDomain(String name) {
        Conf tmpDomain = createDomainRt(name);
        return domainSvc.createDomain(tmpDomain);
    }

    public Conf createDomainRt(String name) {
        //todo не работает
//        MrtService mrtSvc = domainSvc.getModel().bean(MrtService.class);
//        Conf tmp = UtConf.create();
//        Conf tmpDomain = tmp.findConf("domain/" + name, true);
//        tmpDomain.join(this.conf);
//        mrtSvc.prepare(tmp);
//        return tmpDomain;
        return null;
    }

    public List<Field> createFields(Domain forDomain) {
        List<Field> res = new ArrayList<>();

        Conf x = createDomainRt("_tmp_");

        for (Conf x1 : x.getConfs("field")) {
            Field f = domainSvc.createField(x1, forDomain);
            res.add(f);
        }

        return res;
    }

    public Conf addField(String name, String parent) {
        Conf x = conf.findConf("field/" + name, true);
        if (!UtString.empty(parent)) {
            x.setValue("parent", parent);
        }
        return x;
    }

}
