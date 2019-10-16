package jandcode.core.web.gsp.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.web.gsp.*;

/**
 * Создает и инициализирует gsp context.
 */
public class GspContextFactory extends BaseComp implements BeanFactoryOwner {

    private BeanFactory beanFactory = new DefaultBeanFactory();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf conf = cfg.getConf();
        //
        getBeanFactory().beanConfigure(cfg);
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public GspContext createGspContext() {
        GspContextImpl ctx = new GspContextImpl(getApp());
        ctx.getBeanFactory().setParentBeanFactory(getBeanFactory());
        return ctx;
    }

}
