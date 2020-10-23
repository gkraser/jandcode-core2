package jandcode.core.web.impl;

import jandcode.core.*;
import jandcode.core.web.*;

public class RequestContextImpl implements RequestContext {

    private Request request;
    private BeanFactory beanFactory = new DefaultBeanFactory(this);

    public RequestContextImpl(Request request) {
        this.request = request;
    }

    public App getApp() {
        return request.getApp();
    }

    public Request getRequest() {
        return request;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IRequestContextLinkSet) {
            ((IRequestContextLinkSet) inst).setRequestContext(RequestContextImpl.this);
        }
    }

}
