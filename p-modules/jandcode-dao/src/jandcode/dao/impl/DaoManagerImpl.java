package jandcode.dao.impl;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.dao.*;

public class DaoManagerImpl extends BaseComp implements DaoManager {

    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    private DaoProxyFactory daoProxyFactory = new DaoProxyFactory(this);
    private DaoClassDefFactory daoClassDefFactory = new DaoClassDefFactory();


    public Object invokeMethod(DaoMethodDef method, Object... args) throws Exception {

        // создаем контекст
        DaoContext context = createDaoContext();

        // создаем экземпляр dao
        Object daoInst = context.create(method.getCls());

        // выполняем
        Object res = method.getMethod().invoke(daoInst, args);

        //
        return res;
    }

    public <A> A createDao(Class<A> cls) {
        DaoClassDef cd = daoClassDefFactory.getDaoClassDef(cls);
        try {
            return (A) daoProxyFactory.createDaoProxyInst(cd);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    private DaoContext createDaoContext() {
        return new DaoContextImpl(getApp());
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        //todo а надо?
    }

}
