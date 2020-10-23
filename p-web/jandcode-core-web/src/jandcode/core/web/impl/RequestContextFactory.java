package jandcode.core.web.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.web.*;

/**
 * Создает и инициализирует request context.
 */
public class RequestContextFactory extends BaseComp implements BeanFactoryOwner {

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

    public RequestContext createRequestContext(Request request) {
        RequestContextImpl ctx = new RequestContextImpl(request);
        ctx.getBeanFactory().setParentBeanFactory(getBeanFactory());
        return ctx;
    }

}
