package jandcode.dao.impl;

import jandcode.commons.error.*;
import jandcode.dao.*;
import javassist.util.proxy.*;
import javassist.util.proxy.Proxy;

import java.lang.reflect.*;
import java.util.*;

public class DaoProxyFactory {

    private DaoManager daoManager;
    private Map<Class, Class> proxyClasses = new HashMap<>();

    class DaoMethodHandler implements MethodHandler {

        DaoClassDef daoClassDef;

        public DaoMethodHandler(DaoClassDef daoClassDef) {
            this.daoClassDef = daoClassDef;
        }

        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            DaoMethodDef md = daoClassDef.getMethods().find(thisMethod.getName());
            if (md != null) {
                return daoManager.invokeMethod(md, args);
            } else {
                throw new XError("Метод [{0}] класса [{1}] не является методом dao", thisMethod, daoClassDef.getCls().getName());
            }
        }

    }

    public DaoProxyFactory(DaoManager daoManager) {
        this.daoManager = daoManager;
    }

    public Class getDaoProxyClass(DaoClassDef daoClassDef) {
        Class res = proxyClasses.get(daoClassDef.getCls());
        if (res == null) {
            synchronized (this) {
                res = proxyClasses.get(daoClassDef.getCls());
                if (res == null) {
                    res = createDaoProxyClass(daoClassDef);
                    proxyClasses.put(daoClassDef.getCls(), res);
                }
            }
        }
        return res;
    }

    private Class createDaoProxyClass(DaoClassDef daoClassDef) {
        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(daoClassDef.getCls());
        Class clsProxy = f.createClass();
        return clsProxy;
    }

    public Object createDaoProxyInst(DaoClassDef daoClassDef) throws Exception {
        Class proxyClass = getDaoProxyClass(daoClassDef);
        Object res = proxyClass.newInstance();
        ((Proxy) res).setHandler(new DaoMethodHandler(daoClassDef));
        return res;
    }

}
