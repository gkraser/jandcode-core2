package jandcode.core.dbm.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;

public class ModelImpl extends BaseComp implements Model, IBeanIniter {

    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    private Conf conf;
    private ModelDef modelDef;

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IModelMember) {
            ((IModelMember) inst).setModel(this);
        }
    }

    public Conf getConf() {
        return conf;
    }

    //////

    public ModelDef getModelDef() {
        return modelDef;
    }

    public void setModelDef(ModelDef modelDef) {
        this.modelDef = modelDef;
    }

    //////


    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // забирем раскрытую conf
        this.conf = getModelDef().getJoinConf();

        // конфигурим бины
        beanFactory.beanConfigure(new DefaultBeanConfig(this.conf));

        //все, все остальное - как нибудь сами

    }

    ////// IModelDbService

    public DbSource getDbSource() {
        return bean(ModelDbService.class).getDbSource();
    }

}
