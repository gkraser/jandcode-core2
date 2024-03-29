package jandcode.core.dbm.domain.impl;

import jandcode.commons.conf.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.store.*;

public class FieldImpl extends BaseModelMember implements Field, IBeanIniter {

    private Conf conf;
    private Domain domain;
    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    protected DomainServiceImpl domainService;
    protected int index;

    //////

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();

        // bean
        Conf beanConf = this.conf.findConf("bean");
        if (beanConf == null || beanConf.size() == 0) {
            // нет собственных bean
            beanConf = this.domainService.getDomainConfHolder().getFieldBaseConf();
        } else {
            Conf tmp = Conf.create();
            tmp.join(this.domainService.getDomainConfHolder().getFieldBaseConf());
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
        if (inst instanceof IFieldMember) {
            ((IFieldMember) inst).setField(this);
        }
    }

    //////

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Conf getConf() {
        return conf;
    }

    public int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    ////// IField

    protected IField getIField() {
        return bean(IField.class);
    }

    public String getTitle() {
        return getIField().getTitle();
    }

    public String getTitleShort() {
        return getIField().getTitleShort();
    }

    public int getSize() {
        return getIField().getSize();
    }

    public VariantDataType getDataType() {
        return getIField().getDataType();
    }

    public DbDataType getDbDataType() {
        return getIField().getDbDataType();
    }

    public StoreDataType getStoreDataType() {
        return getIField().getStoreDataType();
    }

    public String getSqlType() {
        return getIField().getSqlType();
    }

    public String getSqlValue(Object value) {
        return getIField().getSqlValue(value);
    }

    public String getRef() {
        return getIField().getRef();
    }

    public boolean hasRef() {
        return getIField().hasRef();
    }

    public boolean isRefCascade() {
        return getIField().isRefCascade();
    }

    public boolean isRefIndex() {
        return getIField().isRefIndex();
    }

    public String getDict() {
        return getIField().getDict();
    }

    public boolean hasDict() {
        return getIField().hasDict();
    }

    public int getScale() {
        return getIField().getScale();
    }

    public boolean isReq() {
        return getIField().isReq();
    }

    public boolean isNotNull() {
        return getIField().isNotNull();
    }
    
}
