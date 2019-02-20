package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;

import java.util.*;

public class ServiceHolder implements IServices {

    private Ctx ctx;
    private Map<Class, ICtxService> services = new HashMap<Class, ICtxService>();

    public ServiceHolder(Ctx ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings("unchecked")
    public <A extends ICtxService> A service(Class<A> serviceClass) {
        A res = (A) services.get(serviceClass);
        if (res == null) {
            synchronized (this) {
                res = (A) services.get(serviceClass);
                if (res == null) {
                    if (!CtxService.class.isAssignableFrom(serviceClass)) {
                        throw new XError("Класс {0} должен быть наследником от {1}",
                                serviceClass.getName(), CtxService.class.getName());
                    }
                    res = (A) UtClass.createInst(serviceClass);
                    CtxService resA = (CtxService) res;
                    resA.setCtx(ctx);
                    resA.doCreateThis();
                    //
                    services.put(serviceClass, res);
                }
            }
        }
        return res;
    }

}
