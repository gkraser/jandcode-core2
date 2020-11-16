package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

public class DomainImpl extends BaseModelMember implements Domain, IBeanIniter {

    private Conf conf;
    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    private NamedList<Field> fields = new DefaultNamedList<>("Поле [{0}] не найдено в домене [{1}]", this);
    protected DomainServiceImpl domainService;

    //////

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        this.conf = cfg.getConf();

        // поля
        for (Conf x : this.conf.getConfs("field")) {
            Field f = create(x, FieldImpl.class, this);
            BeanDef pb = f.getBeanFactory().findBean(PrototypeField.class, false);
            if (pb != null) {
                PrototypeField proto = (PrototypeField) pb.createInst();
                List<Field> flds = proto.createFields();
                if (flds != null && flds.size() > 0) {
                    for (Field f1 : flds) {
                        addField((FieldImpl) f1);
                    }
                }
            } else {
                addField((FieldImpl) f);
            }
        }

        // bean
        Conf beanConf = this.conf.findConf("bean");
        if (beanConf == null || beanConf.size() == 0) {
            // нет собственных bean
            beanConf = this.domainService.getDomainConfHolder().getDomainBaseConf();
        } else {
            Conf tmp = UtConf.create();
            tmp.join(this.domainService.getDomainConfHolder().getDomainBaseConf());
            Conf tmp2 = tmp.findConf("bean", true);
            tmp2.join(beanConf);
            beanConf = tmp;
        }
        getBeanFactory().beanConfigure(new DefaultBeanConfig(beanConf));
    }

    //////

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IDomainMember) {
            ((IDomainMember) inst).setDomain(this);
        }
        if (inst instanceof FieldImpl) {
            ((FieldImpl) inst).domainService = this.domainService;
        }
    }

    //////

    public NamedList<Field> getFields() {
        return fields;
    }

    protected void addField(FieldImpl f) {
        f.setDomain(this);
        f.setIndex(fields.size());
        fields.add(f);
    }

    public Conf getConf() {
        return conf;
    }

    ////// IDomain

    protected IDomain getIDomain() {
        return bean(IDomain.class);
    }

    public String getTitle() {
        return getIDomain().getTitle();
    }

    public boolean hasTag(String tag) {
        return getIDomain().hasTag(tag);
    }

    public String getDbTableName() {
        return getIDomain().getDbTableName();
    }

    public Map<String, String> getTags() {
        return getIDomain().getTags();
    }

    //////

}
